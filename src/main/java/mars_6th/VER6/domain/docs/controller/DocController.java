package mars_6th.VER6.domain.docs.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mars_6th.VER6.domain.docs.controller.dto.request.DocRequestDto;
import mars_6th.VER6.domain.docs.exception.DocExceptionType;
import mars_6th.VER6.domain.docs.service.DocService;
import mars_6th.VER6.global.exception.BaseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "문서 조회 컨트롤러", description = "Docs API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/docs")
public class DocController {

    private final DocService docService;

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
    @PutMapping("/{id}")
    public ResponseEntity<?> updateDoc(@PathVariable Long id, @Valid @RequestBody DocRequestDto request) {
        return ResponseEntity.ok(docService.updateDoc(id, request));
    }

    @Operation(summary = "문서 삭제 API")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDoc(@PathVariable Long id) {
        docService.deleteDoc(id);
        return ResponseEntity.ok().build();
    }

}
