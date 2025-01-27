package mars_6th.VER6.domain.docs.controller;

import jakarta.servlet.http.HttpSession;
import mars_6th.VER6.domain.docs.controller.dto.request.MemberRequest;
import mars_6th.VER6.domain.docs.entity.Member;
import mars_6th.VER6.domain.docs.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/loginAction")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<Member> login(@RequestBody MemberRequest req, HttpSession session) {
        Member member = memberService.login(req, session);
        return ResponseEntity.ok(member);
    }
}
