package mars_6th.VER6.domain.minio.service;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mars_6th.VER6.domain.member.entity.Member;
import mars_6th.VER6.domain.member.entity.MemberRole;
import mars_6th.VER6.domain.member.exception.MemberExceptionType;
import mars_6th.VER6.domain.member.repo.MemberRepository;
import mars_6th.VER6.global.exception.BaseException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SessionService {
    private final MemberRepository memberRepository;

    public Member validateSession(HttpSession session) {
        Long userId = (Long) session.getAttribute("id");
        if (userId == null) {
            throw new BaseException(MemberExceptionType.NOT_FOUND_MEMBER);
        }
        return memberRepository.findById(userId)
                .orElseThrow(() -> new BaseException(MemberExceptionType.NOT_FOUND_MEMBER));
    }

    public Member validateTeamLeader(HttpSession session) {
        Member member = validateSession(session);
        if (member.getRole() != MemberRole.TEAM_LEADER) {
            throw new BaseException(MemberExceptionType.NOT_PERMISSION);
        }
        return member;
    }
}

