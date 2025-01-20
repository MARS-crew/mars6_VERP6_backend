package mars_6th.VER6.domain.docs.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mars_6th.VER6.domain.docs.repo.DocsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class DocsService {

    private final DocsRepository docsRepository;

    public void saveDocs() {
        log.info("saveDocs");
    }

    public void getDocs() {
        log.info("getDocs");
    }

    public void updateDocs() {
        log.info("updateDocs");
    }

    public void deleteDocs() {
        log.info("deleteDocs");
    }
}
