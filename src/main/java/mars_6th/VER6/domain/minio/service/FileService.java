package mars_6th.VER6.domain.minio.service;

import io.minio.BucketExistsArgs;
import io.minio.MinioClient;
import io.minio.errors.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mars_6th.VER6.domain.docs.exception.DocExceptionType;
import mars_6th.VER6.domain.minio.dto.PresignedUrlResponseDto;
import mars_6th.VER6.global.exception.BaseException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {

    private final MinioClient minioClient;
    private final MinioService minioService;

    public PresignedUrlResponseDto generatePresignedUploadUrl(HttpSession session) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket("ver6").build());

        if (found) {
            System.out.println("존재");
        } else {
            System.out.println("미존재");
        }

        String generatedFileName = System.currentTimeMillis() + "_" + java.util.UUID.randomUUID().toString();
        session.setAttribute("generatedFileName", generatedFileName);
        String presignedUrl = minioService.getPresignedUploadUrl(generatedFileName, 10);
        log.info("생성된 고유 파일명: {}, Presigned URL: {}", generatedFileName, presignedUrl);
        return new PresignedUrlResponseDto(presignedUrl, generatedFileName);
    }

    public String determineFileUrl(String externalUrl, HttpSession session) {
        if (externalUrl != null && !externalUrl.isBlank()) {
            return externalUrl;
        }
        Object fileNameObj = session.getAttribute("generatedFileName");
        if (fileNameObj == null) {
            throw new BaseException(DocExceptionType.EMPTY_FILE_ERROR);
        }
        return minioService.getFilePath(fileNameObj.toString());
    }

    public String getDownloadUrl(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            throw new BaseException(DocExceptionType.FILE_DOWNLOAD_ERROR);
        }
        if (minioService.isExternalUrl(fileUrl)) {
            return fileUrl;
        }
        return minioService.getPresignedDownloadUrl(minioService.extractObject(fileUrl), 10);
    }

    public void deleteFileIfInternal(String fileUrl) {
        if (fileUrl != null && !fileUrl.isEmpty() && !minioService.isExternalUrl(fileUrl)) {
            minioService.deleteFile(fileUrl);
            log.info("파일 삭제 성공: {}", fileUrl);
        }
    }
}