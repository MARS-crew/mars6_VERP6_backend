package mars_6th.VER6.domain.docs.service;

import lombok.RequiredArgsConstructor;
import mars_6th.VER6.domain.docs.controller.dto.request.DocDetailRequest;
import mars_6th.VER6.domain.docs.controller.dto.request.DocDetailStatusUpdateRequest;
import mars_6th.VER6.domain.docs.controller.dto.response.DocDetailResponse;
import mars_6th.VER6.domain.docs.entity.Doc;
import mars_6th.VER6.domain.docs.entity.DocDetail;
import mars_6th.VER6.domain.docs.exception.DocExceptionType;
import mars_6th.VER6.domain.docs.repo.DocDetailRepository;
import mars_6th.VER6.domain.docs.repo.DocRepository;
import mars_6th.VER6.domain.minio.service.FileService;
import mars_6th.VER6.global.exception.BaseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DocDetailService {

    private final DocRepository docRepository;
    private final DocDetailRepository docDetailRepository;
    private final FileService fileService;

    public List<DocDetailResponse> getDocDetails(Long docId) {
        List<DocDetail> docDetails = docDetailRepository.findDocDetailsByDocId(docId);
        if (docDetails.isEmpty()) {
            throw new BaseException(DocExceptionType.NOT_FOUND_DOC_DETAIL);
        }

        return docDetails.stream()
                .map(DocDetailResponse::of)
                .toList();
    }

    public DocDetailResponse createDocDetail(Long userId, Long docId, DocDetailRequest docDetailRequest, String originalFileName, String externalUrl) {
        Doc doc = docRepository.getDocById(docId);

        DocDetail docDetail;
        if (externalUrl != null && !externalUrl.isBlank()) {
            docDetail = DocDetail.fromFileUrl(userId, docDetailRequest, externalUrl);
        } else {
            String fileUrl = fileService.extractFileUrl(userId);
            docDetail = DocDetail.fromFileName(userId, docDetailRequest, fileUrl);
        }

        docDetail.addDoc(doc);
        doc.markAsUpdated();

        docDetailRepository.save(docDetail);

        return DocDetailResponse.of(docDetail);
    }

    public void updateDocDetailStatus(Long docDetailId, DocDetailStatusUpdateRequest request) {
        DocDetail docDetail = docDetailRepository.getDocDetailById(docDetailId);
        docDetail.updateStatus(request.status());
    }

    public void deleteDocDetail(Long docDetailId) {
        DocDetail docDetail = docDetailRepository.getDocDetailById(docDetailId);

        if (!docDetail.existExternalUrl()) {
            fileService.deleteFileIfInternal(docDetail.getFileName());
        }
        docDetailRepository.delete(docDetail);
    }

}
