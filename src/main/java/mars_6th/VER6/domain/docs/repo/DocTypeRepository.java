package mars_6th.VER6.domain.docs.repo;

import mars_6th.VER6.domain.docs.entity.DocType;
import mars_6th.VER6.domain.docs.exception.DocExceptionType;
import mars_6th.VER6.global.exception.BaseException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocTypeRepository extends JpaRepository<DocType, Long> {

    default DocType getById(Long id) {
        return findById(id).orElseThrow(
                () -> new BaseException(DocExceptionType.NOT_FOUND_DOC_TYPE)
        );
    }

    boolean existsByName(String name);
}
