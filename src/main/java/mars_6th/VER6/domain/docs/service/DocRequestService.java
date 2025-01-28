package mars_6th.VER6.domain.docs.service;

import lombok.RequiredArgsConstructor;
import mars_6th.VER6.domain.docs.controller.DocRequestController;
import mars_6th.VER6.domain.docs.controller.dto.request.DeDocRequestDto;
import mars_6th.VER6.domain.docs.controller.dto.request.DocReqRequestDto;
import mars_6th.VER6.domain.docs.controller.dto.response.DeResponseDto;
import mars_6th.VER6.domain.docs.controller.dto.response.DocReqResponseDto;
import mars_6th.VER6.domain.docs.entity.Doc;
import mars_6th.VER6.domain.docs.entity.DocRequest;
import mars_6th.VER6.domain.docs.entity.DocRequestStatus;
import mars_6th.VER6.domain.docs.repo.DocRepository;
import mars_6th.VER6.domain.docs.repo.DocReqRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DocRequestService {

    private final DocRepository docRepository;
    private final DocReqRepository docReqRepository;
    private final MinioService minioService;

    public List<DocReqResponseDto> getDocReq(Long docId) {

        Doc doc = docRepository.findById(docId)
                .orElseThrow(() -> new RuntimeException("해당 서류를 찾을 수 없습니다."));

        List<DocRequest> docRequests = doc.getDocRequest();

        if (docRequests == null || docRequests.isEmpty()) {
            throw new RuntimeException("해당 서류에 대한 요청이 존재하지 않습니다.");
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
                .orElseThrow(() -> new RuntimeException("해당 문서를 찾을 수 없습니다."));

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
                .orElseThrow(() -> new RuntimeException("서류 요청을 찾을 수 없습니다."));

        String fileUrl = null;

        if (file != null && !file.isEmpty()) {
            fileUrl = minioService.uploadFile(file);
            docRequest.setFileName(file.getOriginalFilename());
        }

        if (url != null && !url.isEmpty()) {
            fileUrl = url;
            docRequest.setFileName(url);
        }

        if (fileUrl != null) {
            docRequest.setFileUrl(fileUrl);
        }

        docRequest.setName(docReqRequestDto.getName());
        docRequest.setContent(docReqRequestDto.getContent());


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
                .orElseThrow(() -> new RuntimeException("서류 요청을 찾을 수 없습니다."));

        if (docRequest.getFileUrl() != null && !docRequest.getFileUrl().isEmpty() && !minioService.isExternalUrl(docRequest.getFileUrl())) {
            minioService.deleteFile(docRequest.getFileUrl());
        }

        docReqRepository.delete(docRequest);
    }

    public DocReqResponseDto changeDocReqStatus(Long reqId, DocRequestStatus newStatus) {
        DocRequest docRequest = docReqRepository.findById(reqId)
                .orElseThrow(() -> new RuntimeException("서류 요청을 찾을 수 없습니다."));

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
