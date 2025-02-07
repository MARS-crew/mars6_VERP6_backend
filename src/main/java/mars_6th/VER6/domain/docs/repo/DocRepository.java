package mars_6th.VER6.domain.docs.repo;

import mars_6th.VER6.domain.docs.entity.Doc;
import mars_6th.VER6.domain.docs.exception.DocExceptionType;
import mars_6th.VER6.global.exception.BaseException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DocRepository extends JpaRepository <Doc, Long> {

    default Doc getById(Long id) {
        return findById(id).orElseThrow(
                () -> new BaseException(DocExceptionType.NOT_FOUND_DOC)
        );
    }

    default List<Doc> getDocsByDocType(Long docTypeId) {
        return findAll().stream()
                .filter(doc -> doc.getDocType().getId().equals(docTypeId))
                .toList();
    }

    @Query("SELECT d FROM Doc d WHERE d.title = :title AND d.version IS NOT NULL AND d.content IS NOT NULL")
    List<Doc> findByTitleAndNotNullFields(String title);

    boolean existsByIsUpdatedTrueAndId(Long docId);
}
