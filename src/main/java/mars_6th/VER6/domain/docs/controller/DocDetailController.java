package mars_6th.VER6.domain.docs.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mars_6th.VER6.domain.docs.controller.dto.request.DeDocRequestDto;
import mars_6th.VER6.domain.docs.controller.dto.response.DeResponseDto;
import mars_6th.VER6.domain.docs.service.DocDetailService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "문서 상세 조회 컨트롤러", description = "Detail Docs API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/docs/detail")
public class DocDetailController {

    private final DocDetailService docDetailService;

    @Operation(summary = "문서 종류별 리스트 조회 API")
    @GetMapping("/{docTitle}")
    public ResponseEntity<List<DeResponseDto>> getDeDocs(@PathVariable String docTitle) {
        List<DeResponseDto> response = docDetailService.getDeDocs(docTitle);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "문서 종류별 리스트 추가 API")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DeResponseDto> createDeDoc(
            @RequestParam Long docId,
            @RequestPart @Valid DeDocRequestDto docRequestDto,
            @RequestPart(required = false) MultipartFile file,
            @RequestParam(required = false) String url) {
        DeResponseDto response = docDetailService.createDeDoc(docId, docRequestDto, file, url);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "문서 종류별 리스트 수정 API")
    @PutMapping(value = "/{docId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DeResponseDto> updateDeDoc(
            @PathVariable Long docId,
            @RequestPart @Valid DeDocRequestDto docRequestDto,
            @RequestPart(required = false) MultipartFile file,
            @RequestParam(required = false) String url) {
        DeResponseDto response = docDetailService.updateDeDoc(docId, docRequestDto, file, url);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "문서 종류별 리스트 삭제 API")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDeDoc(@PathVariable Long id) {
        docDetailService.deleteDoc(id);
        return ResponseEntity.noContent().build();
    }
}
