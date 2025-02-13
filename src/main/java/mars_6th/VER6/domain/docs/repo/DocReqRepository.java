package mars_6th.VER6.domain.docs.repo;

import mars_6th.VER6.domain.docs.entity.Doc;
import mars_6th.VER6.domain.docs.entity.DocRequest;
import mars_6th.VER6.domain.docs.entity.DocRequestStatus;
import mars_6th.VER6.domain.docs.exception.DocExceptionType;
import mars_6th.VER6.global.exception.BaseException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DocReqRepository extends JpaRepository<DocRequest, Long> {

    default DocRequest getDocRequestById(Long reqId) {
        return findById(reqId).orElseThrow(
                () -> new BaseException(DocExceptionType.NOT_FOUND_DOC)
        );
    }

    @Query("SELECT COUNT(dr.id) FROM DocRequest dr WHERE dr.doc = :doc AND dr.status = :status")
    long countByStatus(Doc doc, DocRequestStatus status);
}
