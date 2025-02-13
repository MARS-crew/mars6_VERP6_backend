package mars_6th.VER6.domain.docs.repo;

import mars_6th.VER6.domain.docs.entity.Doc;
import mars_6th.VER6.domain.docs.exception.DocExceptionType;
import mars_6th.VER6.global.exception.BaseException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocRepository extends JpaRepository<Doc, Long> {

    default Doc getDocById(Long docId) {
        return findById(docId).orElseThrow(
                () -> new BaseException(DocExceptionType.NOT_FOUND_DOC)
        );
    }

    Boolean existsByTitle(String title);

    List<Doc> findAllByDocTypeId(Long docTypeId);

    Boolean existsByIsUpdatedTrueAndId(Long docId);
}
