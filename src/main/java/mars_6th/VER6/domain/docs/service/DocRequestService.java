package mars_6th.VER6.domain.docs.service;

import lombok.RequiredArgsConstructor;
import mars_6th.VER6.domain.docs.controller.dto.request.DocReqRequestDto;
import mars_6th.VER6.domain.docs.controller.dto.response.DocReqResponseDto;
import mars_6th.VER6.domain.docs.entity.Doc;
import mars_6th.VER6.domain.docs.entity.DocRequest;
import mars_6th.VER6.domain.docs.entity.DocRequestStatus;
import mars_6th.VER6.domain.docs.repo.DocRepository;
import mars_6th.VER6.domain.docs.repo.DocReqRepository;
import mars_6th.VER6.global.exception.BaseException;
import mars_6th.VER6.global.exception.BaseExceptionType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DocRequestService {

    private final DocRepository docRepository;
    private final DocReqRepository docReqRepository;
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
                        docReq.getName(),
                        docReq.getFileName(),
                        docReq.getStatus(),
                        docReq.getContent(),
                        docReq.getCreatedAt().toLocalDate()
                ))
                .toList();
    }

    public DocReqResponseDto createDocReq(Long docId, DocReqRequestDto docReqRequestDto, MultipartFile file, String url) {
        Doc doc = docRepository.findById(docId)
                .orElseThrow(() -> new BaseException(BaseExceptionType.DOC_NOT_FOUND));

        String fileUrl = null;

        if (file != null && !file.isEmpty()) {
            fileUrl = minioService.uploadFile(file);
        }

        if (url != null && !url.isEmpty()) {
            fileUrl = url;
        }

        DocRequest docRequest = DocRequest.builder()
                .doc(doc)
                .name(docReqRequestDto.getName())
                .title(doc.getVersion() + " " + doc.getFileName())
                .content(docReqRequestDto.getContent())
                .fileName(file != null ? file.getOriginalFilename() : url)
                .fileUrl(fileUrl)
                .status(DocRequestStatus.REQUESTED)
                .build();

        docReqRepository.save(docRequest);

        return new DocReqResponseDto(
                docRequest.getName(),
                docRequest.getFileName(),
                docRequest.getStatus(),
                docRequest.getContent(),
                docRequest.getCreatedAt().toLocalDate()
        );
    }

    public DocReqResponseDto updateDocReq(Long reqId, DocReqRequestDto docReqRequestDto, MultipartFile file, String url) {
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

        docRequest.updateName(docReqRequestDto.getName());
        docRequest.updateContent(docReqRequestDto.getContent());


        docReqRepository.save(docRequest);

        return new DocReqResponseDto(
                docRequest.getName(),
                docRequest.getFileName(),
                docRequest.getStatus(),
                docRequest.getContent(),
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

    public DocReqResponseDto changeDocReqStatus(Long reqId, DocRequestStatus newStatus) {
        DocRequest docRequest = docReqRepository.findById(reqId)
                .orElseThrow(() -> new BaseException(BaseExceptionType.DOC_NOT_FOUND));

        docRequest.changeStatus(newStatus);

        docReqRepository.save(docRequest);

        return new DocReqResponseDto(
                docRequest.getName(),
                docRequest.getFileName(),
                docRequest.getStatus(),
                docRequest.getContent(),
                docRequest.getCreatedAt().toLocalDate()
        );
    }
}
