package mars_6th.VER6.domain.docs.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mars_6th.VER6.domain.docs.controller.dto.request.DocReqRequest;
import mars_6th.VER6.domain.docs.controller.dto.request.DocReqStatusUpdateRequest;
import mars_6th.VER6.domain.docs.controller.dto.response.DocReqResponse;
import mars_6th.VER6.domain.docs.service.DocRequestService;
import mars_6th.VER6.domain.minio.service.SessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "문서 요청 조회 컨트롤러", description = "Request Docs API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/doc-request")
public class DocRequestController {

    private final DocRequestService docRequestService;
    private final SessionService sessionService;

    @Operation(summary = "문서 요청 리스트 조회 API")
    @GetMapping("/{docId}")
    public ResponseEntity<List<DocReqResponse>> getDocRequests(@PathVariable Long docId) {
        return ResponseEntity.ok(docRequestService.getDocReq(docId));
    }

    @Operation(summary = "문서 요청 생성 API")
    @PostMapping("/{docId}")
    public ResponseEntity<DocReqResponse> createDocRequest(
            @PathVariable Long docId,
            @RequestBody @Valid DocReqRequest request,
            HttpSession session) {
        sessionService.validateTeamLeader(session);
        DocReqResponse response = docRequestService.createDocReq(docId, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "문서 요청 상태 수정 API")
    @PutMapping("/{reqId}")
    public ResponseEntity<DocReqResponse> updateDocRequest(
            @PathVariable Long reqId,
            @RequestBody @Valid DocReqStatusUpdateRequest request,
            HttpSession session) {
        sessionService.validateSession(session);
        return ResponseEntity.ok(docRequestService.updateDocReq(reqId, request));
    }

    @Operation(summary = "문서 요청 삭제 API")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocRequest(@PathVariable Long id) {
        docRequestService.deleteDocReq(id);
        return ResponseEntity.noContent().build();
    }

}