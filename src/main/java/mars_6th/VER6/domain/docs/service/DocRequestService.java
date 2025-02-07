package mars_6th.VER6.domain.docs.service;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mars_6th.VER6.domain.docs.controller.dto.request.DocReqRequestDto;
import mars_6th.VER6.domain.docs.controller.dto.response.DocReqResponseDto;
import mars_6th.VER6.domain.docs.controller.dto.response.StatusCountDto;
import mars_6th.VER6.domain.docs.entity.Doc;
import mars_6th.VER6.domain.docs.entity.DocRequest;
import mars_6th.VER6.domain.docs.entity.DocRequestStatus;
import mars_6th.VER6.domain.docs.exception.DocExceptionType;
import mars_6th.VER6.domain.docs.repo.DocRepository;
import mars_6th.VER6.domain.docs.repo.DocReqRepository;
import mars_6th.VER6.domain.member.entity.Member;
import mars_6th.VER6.domain.minio.service.FileService;
import mars_6th.VER6.domain.minio.service.SessionService;
import mars_6th.VER6.global.exception.BaseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class DocRequestService {

    private final DocRepository docRepository;
    private final DocReqRepository docReqRepository;
    private final FileService fileService;
    private final SessionService sessionService;

    public List<DocReqResponseDto> getDocReq(Long docId) {
        Doc doc = docRepository.findById(docId)
                .orElseThrow(() -> new BaseException(DocExceptionType.DOC_NOT_FOUND));

        List<DocRequest> docRequests = doc.getDocRequest();

        if (docRequests == null || docRequests.isEmpty()) {
            throw new BaseException(DocExceptionType.DOC_NOT_FOUND);
        }

        return docRequests.stream()
                .map(docReq -> new DocReqResponseDto(
                        docReq.getContent(),
                        docReq.getFileName(),
                        docReq.getStatus(),
                        docReq.getName(),
                        docReq.getCreatedAt().toLocalDate()
                ))
                .toList();
    }

    public DocReqResponseDto createDocReq(Long docId, DocReqRequestDto docReqRequestDto, String originalFileName, String externalUrl, HttpSession session) {
        Member member = sessionService.validateSession(session);
        Doc doc = docRepository.findById(docId)
                .orElseThrow(() -> new BaseException(DocExceptionType.DOC_NOT_FOUND));

        String fileUrl = fileService.determineFileUrl(externalUrl, session);
        String storedFileName = (originalFileName != null && !originalFileName.isBlank()) ? originalFileName : fileUrl;

        DocRequest docRequest = DocRequest.builder()
                .doc(doc)
                .createdBy(member.getId())
                .name(member.getName())
                .title(doc.getVersion() + " " + doc.getFileName())
                .content(docReqRequestDto.getContent())
                .fileName(storedFileName)
                .fileUrl(fileUrl)
                .status(DocRequestStatus.REQUESTED)
                .build();

        docReqRepository.save(docRequest);
        log.info("문서 요청 생성 성공: {}", docRequest);

        doc.markAsUpdated();

        return toResponseDto(docRequest);
    }


    public DocReqResponseDto updateDocReq(Long reqId, DocReqRequestDto docReqRequestDto, String originalFileName, String externalUrl, HttpSession session) {
        sessionService.validateSession(session);
        DocRequest docRequest = docReqRepository.findById(reqId)
                .orElseThrow(() -> new BaseException(DocExceptionType.DOC_NOT_FOUND));

        String fileUrl = fileService.determineFileUrl(externalUrl, session);
        String storedFileName = (originalFileName != null && !originalFileName.isBlank()) ? originalFileName : fileUrl;

        docRequest.updateFileUrl(fileUrl);
        docRequest.updateFileName(storedFileName);
        docRequest.updateContent(docReqRequestDto.getContent());

        log.info("문서 요청 업데이트 성공: {}", docRequest);
        return toResponseDto(docRequest);
    }

    public void deleteDocReq(Long reqId) {
        DocRequest docRequest = docReqRepository.findById(reqId)
                .orElseThrow(() -> new BaseException(DocExceptionType.DOC_NOT_FOUND));

        fileService.deleteFileIfInternal(docRequest.getFileUrl());
        docReqRepository.delete(docRequest);
        log.info("문서 요청 삭제 성공: {}", reqId);
    }

    public DocReqResponseDto changeDocReqStatus(Long reqId, DocRequestStatus newStatus, HttpSession session) {
        sessionService.validateTeamLeader(session);

        DocRequest docRequest = docReqRepository.findById(reqId)
                .orElseThrow(() -> new BaseException(DocExceptionType.DOC_NOT_FOUND));

        docRequest.changeStatus(newStatus);

        log.info("문서 요청 상태 변경 성공: {}, 새로운 상태: {}", reqId, newStatus);
        return toResponseDto(docRequest);
    }

    public List<StatusCountDto> getRequestCountsByStatus() {
        return docReqRepository.getRequestCountsByStatus();
    }

    private DocReqResponseDto toResponseDto(DocRequest request) {
        return new DocReqResponseDto(
                request.getContent(),
                request.getFileName(),
                request.getStatus(),
                request.getName(),
                request.getCreatedAt().toLocalDate()
        );
    }

    public String getFilePath(Long reqId, HttpSession session) {
        sessionService.validateSession(session);
        DocRequest docRequest = docReqRepository.findById(reqId)
                .orElseThrow(() -> new BaseException(DocExceptionType.DOC_NOT_FOUND));
        return docRequest.getFileUrl();
    }
}
