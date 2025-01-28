package mars_6th.VER6.domain.docs.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mars_6th.VER6.domain.docs.controller.dto.request.DocReqRequestDto;
import mars_6th.VER6.domain.docs.controller.dto.response.DocReqResponseDto;
import mars_6th.VER6.domain.docs.entity.DocRequestStatus;
import mars_6th.VER6.domain.docs.service.DocRequestService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "문서 요청 조회 컨트롤러", description = "Request Docs API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/doc-request")
public class DocRequestController {

    private final DocRequestService docRequestService;

    @Operation(summary = "문서 요청 리스트 조회 API")
    @GetMapping("/{docId}")
    public ResponseEntity<List<DocReqResponseDto>> getDocRequests(@PathVariable Long docId) {
        List<DocReqResponseDto> response = docRequestService.getDocReq(docId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "문서 요청 생성 API")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DocReqResponseDto> createDocRequest(
            @RequestParam Long docId,
            @RequestPart @Valid DocReqRequestDto docReqRequestDto,
            @RequestPart(required = false) MultipartFile file,
            @RequestParam(required = false) String url) {
        DocReqResponseDto response = docRequestService.createDocReq(docId, docReqRequestDto, file, url);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "문서 요청 수정 API")
    @PutMapping(value = "/{reqId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DocReqResponseDto> updateDocRequest(
            @PathVariable Long reqId,
            @RequestPart @Valid DocReqRequestDto docReqRequestDto,
            @RequestPart(required = false) MultipartFile file,
            @RequestParam(required = false) String url) {
        DocReqResponseDto response = docRequestService.updateDocReq(reqId, docReqRequestDto, file, url);
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
            @RequestParam DocRequestStatus status) {
        DocReqResponseDto response = docRequestService.changeDocReqStatus(reqId, status);
        return ResponseEntity.ok(response);
    }
}