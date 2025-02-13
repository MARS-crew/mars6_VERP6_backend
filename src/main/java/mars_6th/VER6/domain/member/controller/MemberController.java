package mars_6th.VER6.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mars_6th.VER6.domain.member.controller.dto.request.MemberRequest;
import mars_6th.VER6.domain.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "회원", description = "회원 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "회원가입", description = "회원가입을 진행합니다.")
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody @Valid MemberRequest req, HttpSession session) {
        return ResponseEntity.ok(memberService.signup(req, session));
    }

    @Operation(summary = "로그인", description = "로그인 시 role 정보는 필수가 아닙니다.")
    @PostMapping
    public ResponseEntity<?> login(@RequestBody @Valid MemberRequest req, HttpSession session) {
        return ResponseEntity.ok(memberService.login(req, session));
    }
}
