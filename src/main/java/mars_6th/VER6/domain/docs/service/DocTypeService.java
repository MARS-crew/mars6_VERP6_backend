package mars_6th.VER6.domain.docs.service;

import lombok.RequiredArgsConstructor;
import mars_6th.VER6.domain.docs.controller.dto.request.DocTypeRequest;
import mars_6th.VER6.domain.docs.controller.dto.response.DocTypeResponse;
import mars_6th.VER6.domain.docs.entity.DocType;
import mars_6th.VER6.domain.docs.repo.DocTypeRepository;
import mars_6th.VER6.domain.exception.DocExceptionType;
import mars_6th.VER6.global.exception.BaseException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DocTypeService {

    private final DocTypeRepository docTypeRepository;

    public List<DocTypeResponse> getDocTypes(Pageable pageable) {
        Page<DocType> page = docTypeRepository.findAll(pageable);
        List<DocType> docTypes = page.getContent();

        return docTypes.stream().map(docType -> DocTypeResponse.of(docType)).toList();
    }

    public DocTypeResponse createDocType(DocTypeRequest request) {
        DocType entity = request.toEntity(1L);

        if (docTypeRepository.existsByName(entity.getName())) {
            throw new BaseException(DocExceptionType.DUPLICATED_DOC_TYPE);
        }

        DocType docType = docTypeRepository.save(entity);

        return DocTypeResponse.of(docType);
    }

    public DocTypeResponse updateDocType(Long id, DocTypeRequest request) {
        DocType docType = docTypeRepository.getById(id);

        docType.updateName(request.title());

        return DocTypeResponse.of(docType);
    }

    public void deleteDocType(Long id) {
        DocType docType = docTypeRepository.getById(id);
        docTypeRepository.delete(docType);
    }
}
