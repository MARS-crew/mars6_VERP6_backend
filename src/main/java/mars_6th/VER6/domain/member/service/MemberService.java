package mars_6th.VER6.domain.member.service;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mars_6th.VER6.domain.member.controller.dto.request.MemberRequest;
import mars_6th.VER6.domain.member.controller.dto.response.MemberResponse;
import mars_6th.VER6.domain.member.entity.Member;
import mars_6th.VER6.domain.member.exception.MemberExceptionType;
import mars_6th.VER6.domain.member.repo.MemberRepository;

import mars_6th.VER6.global.exception.BaseException;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository MemberRepository;

    public MemberResponse signup(MemberRequest req, HttpSession session) {
        if (req.username() == null || req.username().isEmpty() || req.password() == null || req.password().isEmpty() )
            throw new BaseException(MemberExceptionType.EMPTY_USER_INFO);

        if (MemberRepository.existsByUsername(req.username()))
            throw new BaseException(MemberExceptionType.DUPLICATED_USERNAME);

        Member member = req.toEntity();
        MemberRepository.save(member);

        session.setAttribute("id", member.getId());
        session.setMaxInactiveInterval(1800);

        return MemberResponse.from(member, session.getId());
    }

    public MemberResponse login(MemberRequest req, HttpSession session) {
        if (req.username() == null || req.username().isEmpty() || req.password() == null || req.password().isEmpty() )
            throw new BaseException(MemberExceptionType.EMPTY_USER_INFO);

        Member member = MemberRepository.findByUsername(req.username())
                .orElseThrow(() -> new BaseException(MemberExceptionType.INVALID_CREDENTIALS));

        if (!member.getPassword().equals(req.password()))
            throw new BaseException(MemberExceptionType.INVALID_CREDENTIALS);

        session.setAttribute("id", member.getId());
        session.setMaxInactiveInterval(1800);

        return MemberResponse.from(member, session.getId());
    }
}
