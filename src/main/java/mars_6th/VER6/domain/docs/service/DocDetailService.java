package mars_6th.VER6.domain.docs.service;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mars_6th.VER6.domain.docs.controller.dto.request.DeDocRequestDto;
import mars_6th.VER6.domain.docs.controller.dto.response.DeResponseDto;
import mars_6th.VER6.domain.docs.entity.Doc;
import mars_6th.VER6.domain.docs.exception.DocExceptionType;
import mars_6th.VER6.domain.docs.repo.DocRepository;
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
public class DocDetailService {

    private final DocRepository docRepository;
    private final FileService fileService;
    private final SessionService sessionService;


    public List<DeResponseDto> getDeDocs(String docTitle) {
        List<Doc> docs = docRepository.findByTitle(docTitle);
        if (docs.isEmpty()) {
            throw new BaseException(DocExceptionType.DOC_NOT_FOUND);
        }

        return docs.stream()
                .map(doc -> new DeResponseDto(
                        doc.getId(),
                        doc.getVersion(),
                        doc.getFileName(),
                        doc.getContent(),
                        doc.getCreatedAt().toLocalDate()
                ))
                .toList();
    }

    public DeResponseDto createDeDoc(Long docId, DeDocRequestDto deDocRequestDto, String originalFileName, String externalUrl, HttpSession session) {
        Member member = sessionService.validateSession(session);
        Doc existingDoc = docRepository.findById(docId)
                .orElseThrow(() -> new BaseException(DocExceptionType.DOC_NOT_FOUND));

        String fileUrl = fileService.determineFileUrl(externalUrl, session);
        String storedFileName = (originalFileName != null && !originalFileName.isBlank()) ? originalFileName : fileUrl;

        Doc newDoc = Doc.builder()
                .title(existingDoc.getTitle())
                .content(deDocRequestDto.getContent())
                .version(deDocRequestDto.getVersion())
                .createdBy(member.getId())
                .fileName(storedFileName)
                .fileUrl(fileUrl)
                .docType(existingDoc.getDocType())
                .build();

        docRepository.save(newDoc);
        log.info("문서 생성 성공: {}", newDoc);
        return new DeResponseDto(newDoc.getId(), newDoc.getVersion(), newDoc.getFileName(), newDoc.getContent(), newDoc.getCreatedAt().toLocalDate());
    }

    public DeResponseDto updateDeDoc(Long docId, DeDocRequestDto deDocRequestDto, String originalFileName, String externalUrl, HttpSession session) {
        sessionService.validateSession(session);
        Doc doc = docRepository.findById(docId)
                .orElseThrow(() -> new BaseException(DocExceptionType.DOC_NOT_FOUND));

        String fileUrl = fileService.determineFileUrl(externalUrl, session);
        String storedFileName = (originalFileName != null && !originalFileName.isBlank()) ? originalFileName : fileUrl;

        doc.updateFileUrl(fileUrl);
        doc.updateFileName(storedFileName);
        doc.updateVersion(deDocRequestDto.getVersion());
        doc.updateContent(deDocRequestDto.getContent());

        log.info("문서 업데이트 성공: {}", doc);
        return new DeResponseDto(doc.getId(), doc.getVersion(), doc.getFileName(), doc.getContent(), doc.getCreatedAt().toLocalDate());
    }

    public void deleteDoc(Long docId, HttpSession session) {
        sessionService.validateSession(session);
        Doc doc = docRepository.findById(docId)
                .orElseThrow(() -> new BaseException(DocExceptionType.DOC_NOT_FOUND));

        fileService.deleteFileIfInternal(doc.getFileUrl());
        docRepository.delete(doc);
        log.info("문서 삭제 성공: {}", docId);
    }

    public String getFilePath(Long docId, HttpSession session) {
        sessionService.validateSession(session);
        Doc doc = docRepository.findById(docId)
                .orElseThrow(() -> new BaseException(DocExceptionType.DOC_NOT_FOUND));
        return doc.getFileUrl();
    }
}
