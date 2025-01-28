package mars_6th.VER6.domain.docs.service;

import lombok.RequiredArgsConstructor;
import mars_6th.VER6.domain.docs.controller.dto.request.DeDocRequestDto;
import mars_6th.VER6.domain.docs.controller.dto.response.DeResponseDto;
import mars_6th.VER6.domain.docs.entity.Doc;
import mars_6th.VER6.domain.docs.repo.DocRepository;
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
                .orElseThrow(() -> new RuntimeException("문서를 찾을 수 없습니다."));

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
                .orElseThrow(() -> new RuntimeException("서류를 찾을 수 없습니다."));

        String fileUrl = null;

        if (file != null && !file.isEmpty()) {
            fileUrl = minioService.uploadFile(file);
            doc.setFileName(file.getOriginalFilename());
        }

        if (url != null && !url.isEmpty()) {
            fileUrl = url;
            doc.setFileName(url);
        }

        if (fileUrl != null) {
            doc.setFileUrl(fileUrl);
        }

        doc.updateVersion(deDocRequestDto.getVersion());
        doc.setContent(deDocRequestDto.getContent());

        docRepository.save(doc);

        return new DeResponseDto(doc.getVersion(), doc.getFileName(), doc.getCreatedAt().toLocalDate());
    }

    public void deleteDoc(Long docId) {
        Doc doc = docRepository.findById(docId)
                .orElseThrow(() -> new RuntimeException("서류를 찾을 수 없습니다."));

        if (doc.getFileUrl() != null && !doc.getFileUrl().isEmpty() && !minioService.isExternalUrl(doc.getFileUrl())) {
            minioService.deleteFile(doc.getFileUrl());
        }

        docRepository.delete(doc);
    }
}
