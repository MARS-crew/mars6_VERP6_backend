package mars_6th.VER6.domain.docs.repo;

import mars_6th.VER6.domain.docs.entity.DocRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocReqRepository extends JpaRepository <DocRequest, Long> {
}
