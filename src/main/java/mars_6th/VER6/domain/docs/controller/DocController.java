package mars_6th.VER6.domain.docs.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mars_6th.VER6.domain.docs.controller.dto.request.DocRequestDto;
import mars_6th.VER6.domain.docs.service.DocService;
import mars_6th.VER6.domain.minio.service.SessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "문서 조회 컨트롤러", description = "Docs API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/docs")
public class DocController {

    private final DocService docService;
    private final SessionService sessionService;

    @Operation(summary = "문서 조회 API")
    @GetMapping("/{docTypeId}")
    public ResponseEntity<?> getDocs(@PathVariable Long docTypeId) {
        return ResponseEntity.ok(docService.getDocs(docTypeId));
    }

    @Operation(summary = "문서 생성 API")
    @PostMapping
    public ResponseEntity<?> createDoc(
            @Valid @RequestBody DocRequestDto request,
            HttpSession session) {
        return ResponseEntity.ok(docService.createDoc(request, session));
    }

    @Operation(summary = "문서 수정 API")
    @PutMapping("/{docId}")
    public ResponseEntity<?> updateDoc(
            @PathVariable Long docId,
            @Valid @RequestBody DocRequestDto request,
            HttpSession session) {
        sessionService.validateTeamLeader(session);
        return ResponseEntity.ok(docService.updateDoc(docId, request));
    }

    @Operation(summary = "문서 삭제 API")
    @DeleteMapping("/{docId}")
    public ResponseEntity<?> deleteDoc(@PathVariable Long docId, HttpSession session) {
        sessionService.validateTeamLeader(session);
        docService.deleteDoc(docId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "문서 알림 읽기 API", description = "읽지 않은 요청이 있는지 가져옵니다.")
    @GetMapping("/{docId}/has-unread")
    public ResponseEntity<?> hasUnreadDoc(@PathVariable Long docId, HttpSession session) {
        sessionService.validateSession(session);
        return ResponseEntity.ok(docService.hasUnreadDoc(docId));
    }

    @Operation(summary = "문서 알림 읽기 API", description = "문서 요청 알림을 읽음 처리합니다.")
    @PostMapping("/{docId}/read")
    public ResponseEntity<?> readDoc(@PathVariable Long docId, HttpSession session) {
        sessionService.validateSession(session);
        docService.readDoc(docId);
        return ResponseEntity.ok().build();
    }
}
