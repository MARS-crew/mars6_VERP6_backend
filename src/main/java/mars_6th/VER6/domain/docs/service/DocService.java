package mars_6th.VER6.domain.docs.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mars_6th.VER6.domain.docs.repo.DocRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class DocService {

    private final DocRepository docRepository;

    public Object getDoc(Long id) {
        return null;
    }

    public Object createDoc(Object request) {
        return null;
    }

    public Object updateDoc(Long id, Object request) {
        return null;
    }

    public void deleteDoc(Long id) {
    }
}
