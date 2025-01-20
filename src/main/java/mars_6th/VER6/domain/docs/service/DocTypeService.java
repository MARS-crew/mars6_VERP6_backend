package mars_6th.VER6.domain.docs.service;

import lombok.RequiredArgsConstructor;
import mars_6th.VER6.domain.docs.repo.DocTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DocTypeService {

    private final DocTypeRepository docTypeRepository;

    public Object getDocTypes() {
        return null;
    }

    public Object createDocType(Object request) {
        return null;
    }

    public Object updateDocType(Long id, Object request) {
        return null;
    }

    public void deleteDocType(Long id) {
    }
}
