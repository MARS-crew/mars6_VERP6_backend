package mars_6th.VER6.domain.docs.repo;

import mars_6th.VER6.domain.docs.entity.Docs;
import mars_6th.VER6.domain.exception.DocsExceptionType;
import mars_6th.VER6.global.exception.BaseException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocsRepository extends JpaRepository <Docs, Long> {

    default Docs getById(Long id) {
        return findById(id).orElseThrow(
                () -> new BaseException(DocsExceptionType.NOT_FOUND)
        );
    }
}
