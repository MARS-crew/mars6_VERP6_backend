package mars_6th.VER6.domain.docs.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mars_6th.VER6.domain.docs.controller.dto.request.DocReqRequestDto;
import mars_6th.VER6.domain.docs.controller.dto.response.DocReqResponseDto;
import mars_6th.VER6.domain.docs.controller.dto.response.StatusCountDto;
import mars_6th.VER6.domain.docs.entity.DocRequestStatus;
import mars_6th.VER6.domain.docs.service.DocRequestService;
import mars_6th.VER6.domain.minio.service.FileService;
import mars_6th.VER6.global.response.BaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "문서 요청 조회 컨트롤러", description = "Request Docs API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/doc-request")
public class DocRequestController {

    private final DocRequestService docRequestService;
    private final FileService fileService;

    @Operation(summary = "문서 요청 리스트 조회 API")
    @GetMapping("/{docId}")
    public ResponseEntity<List<DocReqResponseDto>> getDocRequests(@PathVariable Long docId) {
        List<DocReqResponseDto> response = docRequestService.getDocReq(docId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "문서 요청 생성 API")
    @PostMapping("/create")
    public ResponseEntity<DocReqResponseDto> createDocRequest(
            @RequestParam Long docId,
            @RequestParam(required = false) String externalUrl,
            @RequestParam(required = false) String originalFileName,
            @RequestBody @Valid DocReqRequestDto docReqRequestDto,
            HttpServletRequest request) {
        HttpSession session = request.getSession();
        DocReqResponseDto response = docRequestService.createDocReq(docId, docReqRequestDto, originalFileName, externalUrl, session);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "문서 요청 수정 API")
    @PutMapping("/{reqId}/update")
    public ResponseEntity<DocReqResponseDto> updateDocRequest(
            @PathVariable Long reqId,
            @RequestParam(required = false) String externalUrl,
            @RequestParam(required = false) String originalFileName,
            @RequestBody @Valid DocReqRequestDto docReqRequestDto,
            HttpServletRequest request) {
        HttpSession session = request.getSession();
        DocReqResponseDto response = docRequestService.updateDocReq(reqId, docReqRequestDto, originalFileName, externalUrl, session);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "문서 요청 삭제 API")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocRequest(@PathVariable Long id) {
        docRequestService.deleteDocReq(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "문서 요청 상태 변경 API")
    @PutMapping("/{reqId}/status")
    public ResponseEntity<DocReqResponseDto> updateDocReqStatus(
            @PathVariable Long reqId,
            @RequestParam DocRequestStatus status,
            HttpServletRequest request) {
        HttpSession session = request.getSession();
        DocReqResponseDto response = docRequestService.changeDocReqStatus(reqId, status, session);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "문서 요쳥 상태별 개수 API")
    @GetMapping("/status/counts")
    public ResponseEntity<List<StatusCountDto>> getRequestCountsByStatus() {
        List<StatusCountDto> statusCounts = docRequestService.getRequestCountsByStatus();
        return ResponseEntity.ok(statusCounts);
    }

    @Operation(summary = "문서 요청 파일 다운로드 API")
    @GetMapping("/{reqId}/download")
    public ResponseEntity<BaseResponse<String>> downloadDocRequest(@PathVariable Long reqId, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String filePath = docRequestService.getFilePath(reqId, session);
        String downloadUrl = fileService.getDownloadUrl(filePath);
        BaseResponse<String> response = new BaseResponse<>(downloadUrl);
        return ResponseEntity.ok(response);
    }
}