package mars_6th.VER6.domain.docs.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mars_6th.VER6.domain.docs.controller.dto.request.DeDocRequestDto;
import mars_6th.VER6.domain.docs.controller.dto.response.DeResponseDto;
import mars_6th.VER6.domain.docs.service.DocDetailService;
import mars_6th.VER6.domain.minio.service.FileService;
import mars_6th.VER6.global.response.BaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "문서 상세 조회 컨트롤러", description = "Detail Docs API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/docs-detail")
public class DocDetailController {

    private final DocDetailService docDetailService;
    private final FileService fileService;

    @Operation(summary = "문서 종류별 리스트 조회 API")
    @GetMapping("/{docTitle}")
    public ResponseEntity<List<DeResponseDto>> getDeDocs(@PathVariable String docTitle) {
        List<DeResponseDto> response = docDetailService.getDeDocs(docTitle);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "문서 종류별 리스트 추가 API")
    @PostMapping("/create")
    public ResponseEntity<DeResponseDto> createDeDoc(
            @RequestParam Long docId,
            @RequestParam(required = false) String externalUrl,
            @RequestParam(required = false) String originalFileName,
            @RequestBody @Valid DeDocRequestDto docRequestDto,
            HttpServletRequest request) {
        HttpSession session = request.getSession();
        DeResponseDto response = docDetailService.createDeDoc(docId, docRequestDto, originalFileName, externalUrl, session);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "문서 종류별 리스트 수정 API")
    @PutMapping(value = "/{docId}/update")
    public ResponseEntity<DeResponseDto> updateDeDoc(
            @PathVariable Long docId,
            @RequestParam(required = false) String externalUrl,
            @RequestParam(required = false) String originalFileName,
            @RequestBody @Valid DeDocRequestDto docRequestDto,
            HttpServletRequest request) {
        HttpSession session = request.getSession();
        DeResponseDto response = docDetailService.updateDeDoc(docId, docRequestDto, originalFileName, externalUrl, session);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "문서 종류별 리스트 삭제 API")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDeDoc(@PathVariable Long id, HttpServletRequest request) {
        HttpSession session = request.getSession();
        docDetailService.deleteDoc(id, session);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "문서 다운로드 API")
    @GetMapping("/{docId}/download")
    public ResponseEntity<BaseResponse<String>> downloadDeDoc(@PathVariable Long docId, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String filePath = docDetailService.getFilePath(docId, session);
        String downloadUrl = fileService.getDownloadUrl(filePath);
        BaseResponse<String> response = new BaseResponse<>(downloadUrl);
        return ResponseEntity.ok(response);
    }
}
