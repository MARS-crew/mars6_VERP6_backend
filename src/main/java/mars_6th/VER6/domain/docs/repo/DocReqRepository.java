package mars_6th.VER6.domain.docs.repo;

import mars_6th.VER6.domain.docs.controller.dto.response.StatusCountDto;
import mars_6th.VER6.domain.docs.entity.Doc;
import mars_6th.VER6.domain.docs.entity.DocRequest;
import mars_6th.VER6.domain.docs.entity.DocRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DocReqRepository extends JpaRepository <DocRequest, Long> {

    @Query("SELECT new mars_6th.VER6.domain.docs.controller.dto.response.StatusCountDto(dr.status, COUNT(dr.id)) " +
            "FROM DocRequest dr GROUP BY dr.status")
    List<StatusCountDto> getRequestCountsByStatus();

    long countByDoc(Doc doc);

    @Query("SELECT COUNT(dr.id) FROM DocRequest dr WHERE dr.doc = :doc AND dr.status = :status")
    long countByStatus(Doc doc, DocRequestStatus status);
}
