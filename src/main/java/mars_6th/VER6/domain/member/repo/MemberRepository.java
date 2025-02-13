package mars_6th.VER6.domain.member.repo;

import mars_6th.VER6.domain.member.exception.MemberExceptionType;
import mars_6th.VER6.global.exception.BaseException;
import org.springframework.data.jpa.repository.JpaRepository;

import mars_6th.VER6.domain.member.entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    default Member getMemberById(Long id) {
        return findById(id).orElseThrow(
                () -> new BaseException(MemberExceptionType.NOT_FOUND_MEMBER)
        );
    }

    boolean existsByUsername(String username);

    Optional<Member> findByUsername(String username);

}
