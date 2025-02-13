package mars_6th.VER6.domain.minio.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mars_6th.VER6.domain.member.entity.Member;
import mars_6th.VER6.domain.minio.dto.PresignedUrlResponse;
import mars_6th.VER6.domain.minio.service.FileService;
import mars_6th.VER6.domain.minio.service.SessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Presigned URL API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/minio")
@Slf4j
public class PresignedUrlController {

    private final FileService fileService;
    private final SessionService sessionService;

    @Operation(summary = "업로드 URL 생성 API")
    @GetMapping("/upload")
    public ResponseEntity<PresignedUrlResponse> generatePresignedUploadUrl(
            HttpSession session
    ) {
        Member member = sessionService.validateSession(session);
        return ResponseEntity.ok(fileService.generatePresignedUploadUrl(member.getId()));
    }

    @Operation(summary = "문서 다운로드 API")
    @GetMapping("/download/{docDetailId}")
    public ResponseEntity<PresignedUrlResponse> downloadDeDoc(@PathVariable Long docDetailId, HttpSession session) {
        sessionService.validateSession(session);
        return ResponseEntity.ok(fileService.getDownloadUrl(docDetailId));
    }
}
