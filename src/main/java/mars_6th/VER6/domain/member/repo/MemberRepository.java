package mars_6th.VER6.domain.member.repo;

import mars_6th.VER6.domain.member.exception.MemberExceptionType;
import mars_6th.VER6.global.exception.BaseException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mars_6th.VER6.domain.member.entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    default Member getById(Long id) {
        return findById(id).orElseThrow(
                () -> new BaseException(MemberExceptionType.NOT_FOUND_MEMBER)
        );
    }

    Optional<Member> findByUsername(String username);

}
