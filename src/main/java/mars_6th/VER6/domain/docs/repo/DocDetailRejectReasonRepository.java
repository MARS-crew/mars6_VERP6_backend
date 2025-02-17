package mars_6th.VER6.domain.docs.repo;

import mars_6th.VER6.domain.docs.entity.DocDetailRejectReason;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocDetailRejectReasonRepository extends JpaRepository<DocDetailRejectReason, Long> {

    List<DocDetailRejectReason> findDocDetailRejectReasonsByDocDetailId(Long docDetailId);
}
