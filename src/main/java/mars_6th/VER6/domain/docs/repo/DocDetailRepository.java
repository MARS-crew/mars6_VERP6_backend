package mars_6th.VER6.domain.docs.repo;

import mars_6th.VER6.domain.docs.entity.DocDetail;
import mars_6th.VER6.domain.docs.exception.DocExceptionType;
import mars_6th.VER6.global.exception.BaseException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DocDetailRepository extends JpaRepository<DocDetail, Long> {

    default DocDetail getDocDetailById(Long id) {
        return findById(id).orElseThrow(
                () -> new BaseException(DocExceptionType.NOT_FOUND_DOC)
        );
    }

    @Query("SELECT d FROM DocDetail d WHERE d.doc.id = :docId")
    List<DocDetail> findDocDetailsByDocId(@Param("docId") Long docId);

    @Query(nativeQuery = true, value = "SELECT file_name FROM tbl_doc_detail WHERE status = 'APPROVED' ORDER BY created_at DESC LIMIT 1")
    String findFileName();

}
