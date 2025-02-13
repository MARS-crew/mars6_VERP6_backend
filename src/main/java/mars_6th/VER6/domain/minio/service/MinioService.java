package mars_6th.VER6.domain.minio.service;

import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mars_6th.VER6.domain.docs.exception.DocExceptionType;
import mars_6th.VER6.global.config.MinioConfig;
import mars_6th.VER6.global.exception.BaseException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
@RequiredArgsConstructor
@Slf4j
public class MinioService {

    private final MinioClient minioClient;
    private final MinioConfig minioConfig;

    public String getPresignedUploadUrl(String filename, int expiryMinutes) {
        ensureBucketExists();

        try {
            String presignedUrl = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.PUT)
                            .bucket(minioConfig.getBucketName())
                            .object(filename)
                            .expiry(expiryMinutes * 60)
                            .build()
            );

            log.info("Presigned Upload URL 생성 성공: {}", presignedUrl);
            return presignedUrl;

        } catch (Exception e) {
            log.error("파일 업로드 URL 생성 중 오류 발생: {}", e.getMessage(), e);
            throw new BaseException(DocExceptionType.FILE_UPLOAD_ERROR);
        }
    }

    public String getPresignedDownloadUrl(String filename, int expiryMinutes) {
        ensureBucketExists();

        try {
            String presignedUrl = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(minioConfig.getBucketName())
                            .object(filename + "\\")
                            .expiry(expiryMinutes * 60)
                            .build()
            );

            log.info("Presigned Download URL 생성 성공: {}", presignedUrl);
            return presignedUrl;

        } catch (Exception e) {
            log.error("파일 다운로드 URL 생성 중 오류 발생: {}", e.getMessage(), e);
            throw new BaseException(DocExceptionType.FILE_DOWNLOAD_ERROR);
        }
    }

    public void deleteFile(String fileName) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(minioConfig.getBucketName())
                            .object(fileName)
                            .build()
            );

            log.info("파일 삭제 성공: {}", fileName);

        } catch (Exception e) {
            log.error("파일 삭제 중 오류 발생: {}", e.getMessage(), e);
            throw new BaseException(DocExceptionType.FILE_DELETE_ERROR);
        }
    }

    public boolean isExternalUrl(String url) {
        return url.startsWith("http://") || url.startsWith("https://");
    }

    private void ensureBucketExists() {
        try {
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(minioConfig.getBucketName()).build())) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(minioConfig.getBucketName()).build());
            }
        } catch (InvalidKeyException | IOException | NoSuchAlgorithmException | InsufficientDataException |
                 ErrorResponseException | InternalException | InvalidResponseException | ServerException |
                 XmlParserException e) {
            log.error("버킷 생성 실패: {}", e.getMessage());
            throw new BaseException(DocExceptionType.MINIO_BUCKET_ERROR);
        }
    }
}
