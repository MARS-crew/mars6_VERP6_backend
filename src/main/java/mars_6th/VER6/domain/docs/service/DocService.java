package mars_6th.VER6.domain.docs.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mars_6th.VER6.domain.docs.controller.dto.request.DocRequestDto;
import mars_6th.VER6.domain.docs.controller.dto.response.DocResponse;
import mars_6th.VER6.domain.docs.entity.Doc;
import mars_6th.VER6.domain.docs.entity.DocType;
import mars_6th.VER6.domain.docs.repo.DocRepository;
import mars_6th.VER6.domain.docs.repo.DocTypeRepository;
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

    public List<DocResponse> getDocs(Long docTypeId) {
        List<Doc> docs = docRepository.getDocsByDocType(docTypeId);

        return docs.stream().map(doc -> DocResponse.of(doc, 0L, 10L)).toList();
    }

    public DocResponse createDoc(DocRequestDto request) {
        Long docTypeId = request.docTypeId();

        DocType docType = docTypeRepository.getById(docTypeId);

        Doc entity = request.toEntity(1L);
        Doc doc = docRepository.save(entity);

        doc.addDocType(docType);

        return DocResponse.of(doc, 0L, 10L);
    }

    public DocResponse updateDoc(Long id, DocRequestDto request) {
        Doc doc = docRepository.getById(id);
        doc.updateName(request.title());

        return DocResponse.of(doc, 0L, 10L);
    }

    public void deleteDoc(Long id) {
        Doc doc = docRepository.getById(id);
        docRepository.delete(doc);
    }
}
