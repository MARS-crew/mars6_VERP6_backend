package mars_6th.VER6.domain.docs.service;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mars_6th.VER6.domain.docs.controller.dto.request.MemberRequest;
import mars_6th.VER6.domain.docs.entity.Member;
import mars_6th.VER6.domain.docs.repo.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository MemberRepository;

    public Member login(MemberRequest req, HttpSession  session) {

        Optional<Member> optionalMember = MemberRepository.findByUsername(req.getUsername());
        Member user = optionalMember.get();

        if(!user.getPassword().equals(req.getPassword())){
            throw new IllegalArgumentException("아이디 또는 비밀번호가 잘못되었습니다.");
        }
        session.setAttribute("id", user.getId());
        session.setMaxInactiveInterval(1800);

        return user;
    }
}
