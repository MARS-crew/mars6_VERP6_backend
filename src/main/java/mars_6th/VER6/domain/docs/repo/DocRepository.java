package mars_6th.VER6.domain.docs.repo;

import mars_6th.VER6.domain.docs.entity.Doc;
import mars_6th.VER6.domain.exception.DocExceptionType;
import mars_6th.VER6.global.exception.BaseException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocRepository extends JpaRepository <Doc, Long> {

    default Doc getById(Long id) {
        return findById(id).orElseThrow(
                () -> new BaseException(DocExceptionType.NOT_FOUND_DOC)
        );
    }
}
