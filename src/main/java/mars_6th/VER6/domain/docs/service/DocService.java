package mars_6th.VER6.domain.docs.service;

import lombok.RequiredArgsConstructor;
import mars_6th.VER6.domain.docs.controller.dto.request.DocRequestDto;
import mars_6th.VER6.domain.docs.controller.dto.response.DocResponse;
import mars_6th.VER6.domain.docs.entity.Doc;
import mars_6th.VER6.domain.docs.entity.DocRequestStatus;
import mars_6th.VER6.domain.docs.entity.DocType;
import mars_6th.VER6.domain.docs.exception.DocExceptionType;
import mars_6th.VER6.domain.docs.repo.DocDetailRepository;
import mars_6th.VER6.domain.docs.repo.DocRepository;
import mars_6th.VER6.domain.docs.repo.DocReqRepository;
import mars_6th.VER6.domain.docs.repo.DocTypeRepository;
import mars_6th.VER6.global.exception.BaseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DocService {

    private final DocRepository docRepository;
    private final DocDetailRepository docDetailRepository;
    private final DocTypeRepository docTypeRepository;
    private final DocReqRepository docReqRepository;

    public List<DocResponse> getDocs(Long docTypeId) {
        List<Doc> docs = docRepository.findAllByDocTypeId(docTypeId);

        return docs.stream()
                .map(this::getDocResponse)
                .toList();
    }

    public DocResponse createDoc(DocRequestDto request) {
        Long docTypeId = request.docTypeId();
        DocType docType = docTypeRepository.getDocTypeById(docTypeId);

        if (docRepository.existsByTitle(request.title())) {
            throw new BaseException(DocExceptionType.DUPLICATED_DOC);
        }

        Doc doc = docRepository.save(request.toEntity());
        doc.addDocType(docType);

        return getDocResponse(doc);
    }

    public DocResponse updateDoc(Long docId, DocRequestDto request) {
        Doc doc = docRepository.getDocById(docId);

        if (docRepository.existsByTitle(request.title())) {
            throw new BaseException(DocExceptionType.DUPLICATED_DOC);
        }
        doc.updateTitle(request.title());

        return getDocResponse(doc);
    }

    public void deleteDoc(Long docId) {
        if (!docRepository.existsById(docId)) {
            throw new BaseException(DocExceptionType.NOT_FOUND_DOC);
        }

        docRepository.deleteById(docId);
    }

    public void readDocAlarm(Long docId) {
        Doc doc = docRepository.getDocById(docId);
        doc.markAsRead();
    }

    public boolean hasUnreadDoc(Long docId) {
        return docRepository.existsByIsUpdatedTrueAndId(docId);
    }

    private DocResponse getDocResponse(Doc doc) {
        String fileName = docDetailRepository.findFileName();
        Long completedRequestStep = docReqRepository.countByStatus(doc, DocRequestStatus.COMPLETED);
        Long inProgressRequestStep = docReqRepository.countByStatus(doc, DocRequestStatus.IN_PROGRESS);
        Long pendingRequestStep = docReqRepository.countByStatus(doc, DocRequestStatus.PENDING);

        return DocResponse.of(doc, fileName, completedRequestStep, inProgressRequestStep, pendingRequestStep);
    }
}
