package mars_6th.VER6.domain.member.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import mars_6th.VER6.domain.member.controller.dto.request.MemberRequest;
import mars_6th.VER6.domain.member.controller.dto.response.MemberResponse;
import mars_6th.VER6.domain.member.entity.Member;
import mars_6th.VER6.domain.member.service.MemberService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/loginAction")
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<?> login(@RequestBody MemberRequest req, HttpSession session) {
        return ResponseEntity.ok(memberService.login(req, session));
    }
}
