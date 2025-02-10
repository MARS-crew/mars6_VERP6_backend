package mars_6th.VER6.domain.docs.service;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mars_6th.VER6.domain.docs.controller.dto.request.DocRequestDto;
import mars_6th.VER6.domain.docs.controller.dto.response.DocResponse;
import mars_6th.VER6.domain.docs.entity.Doc;
import mars_6th.VER6.domain.docs.entity.DocRequestStatus;
import mars_6th.VER6.domain.docs.entity.DocType;
import mars_6th.VER6.domain.docs.repo.DocRepository;
import mars_6th.VER6.domain.docs.repo.DocReqRepository;
import mars_6th.VER6.domain.docs.repo.DocTypeRepository;
import mars_6th.VER6.domain.member.entity.Member;
import mars_6th.VER6.domain.minio.service.SessionService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class DocService {

    private final DocRepository docRepository;
    private final DocTypeRepository docTypeRepository;
    private final DocReqRepository docReqRepository;
    private final SessionService sessionService;

    public List<DocResponse> getDocs(Long docTypeId) {
        List<Doc> docs = docRepository.getDocsByDocType(docTypeId);
        return docs.stream().map(this::getDocResponse).toList();
    }

    public DocResponse createDoc(DocRequestDto request, HttpSession session) {
        Long docTypeId = request.docTypeId();

        DocType docType = docTypeRepository.getById(docTypeId);

        Member member = sessionService.validateTeamLeader(session);
        Doc entity = request.toEntity(member.getId());
        Doc doc = docRepository.save(entity);

        doc.addDocType(docType);

        return getDocResponse(doc);
    }

    public DocResponse updateDoc(Long id, DocRequestDto request) {
        Doc doc = docRepository.getById(id);
        doc.updateName(request.title());

        return getDocResponse(doc);
    }

    public void deleteDoc(Long id) {
        Doc doc = docRepository.getById(id);
        docRepository.delete(doc);
    }

    public void readDoc(Long id) {
        Doc doc = docRepository.getById(id);
        doc.markAsRead();
    }

    public boolean hasUnreadDoc(Long id) {
        return docRepository.existsByIsUpdatedTrueAndId(id);
    }

    @NotNull
    private DocResponse getDocResponse(Doc doc) {
        long totalCount = docReqRepository.countByDoc(doc);
        long completedCount = docReqRepository.countByStatus(doc, DocRequestStatus.COMPLETED);
        long inProgressCount = docReqRepository.countByStatus(doc, DocRequestStatus.IN_PROGRESS);
        long canceledCount = docReqRepository.countByStatus(doc, DocRequestStatus.CANCELED);
        return DocResponse.of(doc, completedCount, inProgressCount, canceledCount, totalCount);
    }
}
