package mars_6th.VER6.domain.minio.controller;

import io.minio.errors.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import mars_6th.VER6.domain.minio.dto.PresignedUrlResponseDto;
import mars_6th.VER6.domain.minio.service.FileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Tag(name = "Presigned URL API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/minio")
public class PresignedUrlController {

    private final FileService fileService;

    @Operation(summary = "업로드 URL 생성 API")
    @GetMapping("/upload")
    public ResponseEntity<PresignedUrlResponseDto> generatePresignedUploadUrl(HttpServletRequest request) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        HttpSession session = request.getSession();
        PresignedUrlResponseDto response = fileService.generatePresignedUploadUrl(session);
        return ResponseEntity.ok(response);
    }
}
