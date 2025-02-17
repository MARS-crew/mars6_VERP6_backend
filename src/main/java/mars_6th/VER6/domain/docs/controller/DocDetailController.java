package mars_6th.VER6.domain.docs.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mars_6th.VER6.domain.docs.controller.dto.request.DocDetailRejectReasonRequest;
import mars_6th.VER6.domain.docs.controller.dto.request.DocDetailRequest;
import mars_6th.VER6.domain.docs.controller.dto.request.DocDetailStatusUpdateRequest;
import mars_6th.VER6.domain.docs.controller.dto.response.DocDetailRejectReasonResponse;
import mars_6th.VER6.domain.docs.controller.dto.response.DocDetailResponse;
import mars_6th.VER6.domain.docs.service.DocDetailService;
import mars_6th.VER6.domain.member.entity.Member;
import mars_6th.VER6.domain.minio.service.SessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "문서 상세 조회 컨트롤러", description = "Detail Docs API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/docs-detail")
public class DocDetailController {

    private final SessionService sessionService;
    private final DocDetailService docDetailService;

    @Operation(summary = "문서 종류별 리스트 조회 API")
    @GetMapping("/{docId}")
    public ResponseEntity<List<DocDetailResponse>> getDocDetails(@PathVariable Long docId) {
        return ResponseEntity.ok(docDetailService.getDocDetails(docId));
    }

    @Operation(summary = "문서 종류별 리스트 추가 API")
    @PostMapping("/{docId}")
    public ResponseEntity<DocDetailResponse> createDocDetail(
            @PathVariable Long docId,

            @Parameter(description = "외부 URL을 입력해야 합니다")
            @RequestParam(required = false) String externalUrl,

            @Parameter(description = "presigned-url 을 통해 생성된 generatedFileName을 입력해야 합니다")
            @RequestParam(required = false) String uploadFileUrl,

            @RequestParam(required = false) String fileName,

            @RequestBody @Valid DocDetailRequest request,
            HttpSession session) {
        Member member = sessionService.validateSession(session);
        return ResponseEntity.ok(docDetailService.createDocDetail(
                member.getId(), docId, request, uploadFileUrl, fileName, externalUrl));
    }

    @Operation(summary = "문서 상태 수정 API", description = "문서 상태를 수정합니다. \n" +
            "상태 : [APPROVED, REJECTED, CHECKED, PENDING]")
    @PutMapping("/{docDetailId}")
    public ResponseEntity<DocDetailResponse> updateDocDetailStatus(
            @PathVariable Long docDetailId,
            @RequestBody @Valid DocDetailStatusUpdateRequest request,
            HttpSession session) {
        sessionService.validateTeamLeader(session);
        docDetailService.updateDocDetailStatus(docDetailId, request);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "문서 종류별 리스트 삭제 API")
    @DeleteMapping("/{docDetailId}")
    public ResponseEntity<Void> deleteDeDoc(@PathVariable Long docDetailId, HttpSession session) {
        sessionService.validateSession(session);
        docDetailService.deleteDocDetail(docDetailId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "거절 사유 추가 API")
    @PostMapping("/{docDetailId}/reject-reason")
    public ResponseEntity<DocDetailRejectReasonResponse> addRejectReason(
            @PathVariable Long docDetailId,
            @RequestBody DocDetailRejectReasonRequest request,
            HttpSession session) {
        sessionService.validateTeamLeader(session);
        return ResponseEntity.ok(docDetailService.addRejectReason(docDetailId, request));
    }
}
