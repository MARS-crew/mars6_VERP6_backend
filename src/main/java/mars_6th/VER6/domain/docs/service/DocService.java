package mars_6th.VER6.domain.docs.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mars_6th.VER6.domain.docs.controller.dto.request.DocRequest;
import mars_6th.VER6.domain.docs.controller.dto.response.DocResponse;
import mars_6th.VER6.domain.docs.entity.Doc;
import mars_6th.VER6.domain.docs.repo.DocRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class DocService {

    private final DocRepository docRepository;

    public List<DocResponse> getDocs(Long docTypeId) {
        List<Doc> docs = docRepository.getDocsByDocType(docTypeId);

        return docs.stream().map(doc -> DocResponse.of(doc, 0L, 10L)).toList();
    }

    public DocResponse createDoc(DocRequest request) {
        Doc entity = request.toEntity(1L);
        Doc doc = docRepository.save(entity);

        return DocResponse.of(doc, 0L, 10L);
    }

    public DocResponse updateDoc(Long id, DocRequest request) {
        Doc doc = docRepository.getById(id);
        doc.updateName(request.title());

        return DocResponse.of(doc, 0L, 10L);
    }

    public void deleteDoc(Long id) {
        Doc doc = docRepository.getById(id);
        docRepository.delete(doc);
    }
}
