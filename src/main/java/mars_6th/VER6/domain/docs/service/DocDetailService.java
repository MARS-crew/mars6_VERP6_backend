package mars_6th.VER6.domain.docs.service;

import lombok.RequiredArgsConstructor;
import mars_6th.VER6.domain.docs.controller.dto.request.DeDocRequestDto;
import mars_6th.VER6.domain.docs.controller.dto.response.DeResponseDto;
import mars_6th.VER6.domain.docs.entity.Doc;
import mars_6th.VER6.domain.docs.repo.DocRepository;
import mars_6th.VER6.global.exception.BaseException;
import mars_6th.VER6.global.exception.BaseExceptionType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DocDetailService {

    private final DocRepository docRepository;
    private final MinioService minioService;

    public List<DeResponseDto> getDeDocs(String docTitle) {
        List<Doc> docs = docRepository.findByTitle(docTitle);
        if (docs.isEmpty()) {
            throw new BaseException(BaseExceptionType.DOC_NOT_FOUND);
        }

        return docs.stream()
                .map(doc -> new DeResponseDto(
                        doc.getVersion(),
                        doc.getFileName(),
                        doc.getCreatedAt().toLocalDate()
                ))
                .toList();
    }

    public DeResponseDto createDeDoc(Long docId, DeDocRequestDto deDocRequestDto, MultipartFile file, String url) {

        Doc existingDoc = docRepository.findById(docId)
                .orElseThrow(() -> new BaseException(BaseExceptionType.DOC_NOT_FOUND));

        String fileUrl = null;

        if (file != null && !file.isEmpty()) {
            fileUrl = minioService.uploadFile(file);
        }

        if (url != null && !url.isEmpty()) {
            fileUrl = url;
        }

        Doc newDoc = Doc.builder()
                .title(existingDoc.getTitle())
                .content(deDocRequestDto.getContent())
                .version(deDocRequestDto.getVersion())
                .fileName(file != null ? file.getOriginalFilename() : url)
                .fileUrl(fileUrl)
                .docType(existingDoc.getDocType())
                .build();

        docRepository.save(newDoc);

        return new DeResponseDto(newDoc.getVersion(), newDoc.getFileName(), newDoc.getCreatedAt().toLocalDate());
    }

    public DeResponseDto updateDeDoc(Long docId, DeDocRequestDto deDocRequestDto, MultipartFile file, String url) {
        Doc doc = docRepository.findById(docId)
                .orElseThrow(() -> new BaseException(BaseExceptionType.DOC_NOT_FOUND));

        String fileUrl = null;

        if (file != null && !file.isEmpty()) {
            fileUrl = minioService.uploadFile(file);
            doc.updateFileName(file.getOriginalFilename());
        }

        if (url != null && !url.isEmpty()) {
            fileUrl = url;
            doc.updateFileName(url);
        }

        if (fileUrl != null) {
            doc.updateFileUrl(fileUrl);
        }

        doc.updateVersion(deDocRequestDto.getVersion());
        doc.updateContent(deDocRequestDto.getContent());

        docRepository.save(doc);

        return new DeResponseDto(doc.getVersion(), doc.getFileName(), doc.getCreatedAt().toLocalDate());
    }

    public void deleteDoc(Long docId) {
        Doc doc = docRepository.findById(docId)
                .orElseThrow(() -> new BaseException(BaseExceptionType.DOC_NOT_FOUND));

        if (doc.getFileUrl() != null && !doc.getFileUrl().isEmpty() && !minioService.isExternalUrl(doc.getFileUrl())) {
            minioService.deleteFile(doc.getFileUrl());
        }

        docRepository.delete(doc);
    }
}
