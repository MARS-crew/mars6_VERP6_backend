package mars_6th.VER6.domain.docs.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mars_6th.VER6.domain.docs.controller.dto.request.DocTypeCreateRequest;
import mars_6th.VER6.domain.docs.service.DocTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Tag(name = "문서 타입(리스트) 조회 컨트롤러", description = "Docs Type API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/doc-types")
public class DocTypeController {

    private final DocTypeService docTypeService;

    @Operation(summary = "문서 타입(리스트) 조회 API")
    @GetMapping
    public ResponseEntity<?> getDocTypes() {
        return ResponseEntity.ok(docTypeService.getDocTypes());
    }

    @Operation(summary = "문서 타입 생성 API")
    @PostMapping
    public ResponseEntity<?> createDocType(@Valid @RequestBody DocTypeCreateRequest request) {
        return ResponseEntity.ok(docTypeService.createDocType(request));
    }

    @Operation(summary = "문서 타입 수정 API")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateDocType(@PathVariable Long id, @Valid @RequestBody DocTypeCreateRequest request) {
        return ResponseEntity.ok(docTypeService.updateDocType(id, request));
    }

    @Operation(summary = "문서 타입 삭제 API")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDocType(@PathVariable Long id) {
        docTypeService.deleteDocType(id);
        return ResponseEntity.ok().build();
    }
}
