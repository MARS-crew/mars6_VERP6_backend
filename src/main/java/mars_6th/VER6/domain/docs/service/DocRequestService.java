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
import mars_6th.VER6.domain.docs.repo.DocRepository;
import mars_6th.VER6.domain.docs.repo.DocReqRepository;
import mars_6th.VER6.domain.member.entity.Member;
import mars_6th.VER6.domain.member.entity.MemberRole;
import mars_6th.VER6.domain.member.exception.MemberExceptionType;
import mars_6th.VER6.domain.member.repo.MemberRepository;
import mars_6th.VER6.global.exception.BaseException;
import mars_6th.VER6.global.exception.BaseExceptionType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class DocRequestService {

    private final DocRepository docRepository;
    private final DocReqRepository docReqRepository;
    private final MemberRepository memberRepository;
    private final MinioService minioService;

    public List<DocReqResponseDto> getDocReq(Long docId) {

        Doc doc = docRepository.findById(docId)
                .orElseThrow(() -> new BaseException(BaseExceptionType.DOC_NOT_FOUND));

        List<DocRequest> docRequests = doc.getDocRequest();

        if (docRequests == null || docRequests.isEmpty()) {
            throw new BaseException(BaseExceptionType.DOC_NOT_FOUND);
        }

        return doc.getDocRequest().stream()
                .map(docReq -> new DocReqResponseDto(
                        docReq.getContent(),
                        docReq.getFileName(),
                        docReq.getStatus(),
                        docReq.getName(),
                        docReq.getCreatedAt().toLocalDate()
                ))
                .toList();
    }

    public DocReqResponseDto createDocReq(Long docId, DocReqRequestDto docReqRequestDto, MultipartFile file, String url, HttpSession session) {

        Long userId = (Long) session.getAttribute("id");

        if (userId == null) {
            throw new BaseException(MemberExceptionType.NOT_FOUND_MEMBER);
        }

        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new BaseException(MemberExceptionType.NOT_FOUND_MEMBER));

        Doc doc = docRepository.findById(docId)
                .orElseThrow(() -> new BaseException(BaseExceptionType.DOC_NOT_FOUND));

        String fileUrl = null;

        if (file != null && !file.isEmpty()) {
            fileUrl = minioService.uploadFile(file);
        }

        if (url != null && !url.isEmpty()) {
            fileUrl = url;
        }

        log.info("유저 이름: " + member.getName());
        log.info("유저 아이디: " + member.getUsername());
        DocRequest docRequest = DocRequest.builder()
                .doc(doc)
                .createdBy(member.getId())
                .name(member.getName())
                .title(doc.getVersion() + " " + doc.getFileName())
                .content(docReqRequestDto.getContent())
                .fileName(file != null ? file.getOriginalFilename() : url)
                .fileUrl(fileUrl)
                .status(DocRequestStatus.REQUESTED)
                .build();

        docReqRepository.save(docRequest);

        return new DocReqResponseDto(
                docRequest.getContent(),
                docRequest.getFileName(),
                docRequest.getStatus(),
                docRequest.getName(),
                docRequest.getCreatedAt().toLocalDate()
        );
    }

    public DocReqResponseDto updateDocReq(Long reqId, DocReqRequestDto docReqRequestDto, MultipartFile file, String url, HttpSession session) {

        Long userId = (Long) session.getAttribute("id");

        if (userId == null) {
            throw new BaseException(MemberExceptionType.NOT_FOUND_MEMBER);
        }

        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new BaseException(MemberExceptionType.NOT_FOUND_MEMBER));

        DocRequest docRequest = docReqRepository.findById(reqId)
                .orElseThrow(() -> new BaseException(BaseExceptionType.DOC_NOT_FOUND));

        String fileUrl = null;

        if (file != null && !file.isEmpty()) {
            fileUrl = minioService.uploadFile(file);
            docRequest.updateFileName(file.getOriginalFilename());
        }

        if (url != null && !url.isEmpty()) {
            fileUrl = url;
            docRequest.updateFileName(url);
        }

        if (fileUrl != null) {
            docRequest.updateFileUrl(fileUrl);
        }

        docRequest.updateName(member.getUsername());
        docRequest.updateContent(docReqRequestDto.getContent());


        docReqRepository.save(docRequest);

        return new DocReqResponseDto(
                docRequest.getContent(),
                docRequest.getFileName(),
                docRequest.getStatus(),
                docRequest.getName(),
                docRequest.getCreatedAt().toLocalDate()
        );
    }

    public void deleteDocReq(Long reqId) {
        DocRequest docRequest = docReqRepository.findById(reqId)
                .orElseThrow(() -> new BaseException(BaseExceptionType.DOC_NOT_FOUND));

        if (docRequest.getFileUrl() != null && !docRequest.getFileUrl().isEmpty() && !minioService.isExternalUrl(docRequest.getFileUrl())) {
            minioService.deleteFile(docRequest.getFileUrl());
        }

        docReqRepository.delete(docRequest);
    }

    public DocReqResponseDto changeDocReqStatus(Long reqId, DocRequestStatus newStatus, HttpSession session) {

        Long userId = (Long) session.getAttribute("id");
        if (userId == null) {
            throw new BaseException(MemberExceptionType.NOT_FOUND_MEMBER);
        }
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new BaseException(MemberExceptionType.NOT_FOUND_MEMBER));

        if (member.getRole() != MemberRole.TEAM_LEADER) {
            throw new BaseException(MemberExceptionType.NOT_PERMISSION);
        }

        DocRequest docRequest = docReqRepository.findById(reqId)
                .orElseThrow(() -> new BaseException(BaseExceptionType.DOC_NOT_FOUND));

        docRequest.changeStatus(newStatus);

        docReqRepository.save(docRequest);

        return new DocReqResponseDto(
                docRequest.getContent(),
                docRequest.getFileName(),
                docRequest.getStatus(),
                docRequest.getName(),
                docRequest.getCreatedAt().toLocalDate()
        );
    }

    public List<StatusCountDto> getRequestCountsByStatus() {
        return docReqRepository.getRequestCountsByStatus();
    }
}
