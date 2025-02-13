package mars_6th.VER6.domain.docs.service;

import lombok.RequiredArgsConstructor;
import mars_6th.VER6.domain.docs.controller.dto.request.DocReqRequest;
import mars_6th.VER6.domain.docs.controller.dto.request.DocReqStatusUpdateRequest;
import mars_6th.VER6.domain.docs.controller.dto.response.DocReqResponse;
import mars_6th.VER6.domain.docs.entity.Doc;
import mars_6th.VER6.domain.docs.entity.DocRequest;
import mars_6th.VER6.domain.docs.exception.DocExceptionType;
import mars_6th.VER6.domain.docs.repo.DocDetailRepository;
import mars_6th.VER6.domain.docs.repo.DocRepository;
import mars_6th.VER6.domain.docs.repo.DocReqRepository;
import mars_6th.VER6.global.exception.BaseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DocRequestService {

    private final DocDetailRepository docDetailRepository;
    private final DocReqRepository docReqRepository;
    private final DocRepository docRepository;

    public List<DocReqResponse> getDocReq(Long docId) {
        Doc doc = docRepository.getDocById(docId);

        List<DocRequest> docRequests = doc.getDocRequests();

        return docRequests.stream()
                .map(DocReqResponse::of)
                .toList();
    }

    public DocReqResponse createDocReq(Long docId, DocReqRequest request) {
        Doc doc = docRepository.getDocById(docId);

        DocRequest docRequest = request.toEntity();

        docRequest.addDoc(doc);

        docReqRepository.save(docRequest);

        doc.markAsUpdated();

        return DocReqResponse.of(docRequest);
    }

    public DocReqResponse updateDocReq(Long reqId, DocReqStatusUpdateRequest request) {
        DocRequest docRequest = docReqRepository.getDocRequestById(reqId);

        docRequest.updateStatus(request.status());

        return DocReqResponse.of(docRequest);
    }

    public void deleteDocReq(Long reqId) {
        DocRequest docRequest = docReqRepository.findById(reqId)
                .orElseThrow(() -> new BaseException(DocExceptionType.DOC_NOT_FOUND));

        docReqRepository.delete(docRequest);
    }

}
