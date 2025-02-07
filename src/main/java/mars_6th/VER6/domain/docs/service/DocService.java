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
        return docs.stream().map(doc -> {
            long totalCount = docReqRepository.countByDoc(doc);
            long completedCount = docReqRepository.countByCompleted(doc, DocRequestStatus.COMPLETED);
            return DocResponse.of(doc, completedCount, totalCount);
        }).toList();
    }

    public DocResponse createDoc(DocRequestDto request, HttpSession session) {
        Long docTypeId = request.docTypeId();

        DocType docType = docTypeRepository.getById(docTypeId);

        Member member = sessionService.validateTeamLeader(session);
        Doc entity = request.toEntity(member.getId());
        Doc doc = docRepository.save(entity);

        doc.addDocType(docType);

        long totalCount = docReqRepository.countByDoc(doc);
        long completedCount = docReqRepository.countByCompleted(doc, DocRequestStatus.COMPLETED);

        return DocResponse.of(doc, completedCount, totalCount);
    }

    public DocResponse updateDoc(Long id, DocRequestDto request) {
        Doc doc = docRepository.getById(id);
        doc.updateName(request.title());

        long totalCount = docReqRepository.countByDoc(doc);
        long completedCount = docReqRepository.countByCompleted(doc, DocRequestStatus.COMPLETED);

        return DocResponse.of(doc, completedCount, totalCount);
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
}
