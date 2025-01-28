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

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository MemberRepository;

    public MemberResponse login(MemberRequest req, HttpSession  session) {

        Member member = MemberRepository.findByUsername(req.username())
                .orElseThrow(() -> new BaseException(MemberExceptionType.NOT_FOUND_MEMBER));

        if(!member.getPassword().equals(req.password())){
            throw new IllegalArgumentException("아이디 또는 비밀번호가 잘못되었습니다.");
        }
        session.setAttribute("id", member.getId());
        session.setMaxInactiveInterval(1800);

        return MemberResponse.from(member, session.getId());
    }
}
