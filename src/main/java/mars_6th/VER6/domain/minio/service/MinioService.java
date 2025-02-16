package mars_6th.VER6.domain.minio.service;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mars_6th.VER6.domain.docs.exception.DocExceptionType;
import mars_6th.VER6.global.config.MinioConfig;
import mars_6th.VER6.global.exception.BaseException;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
@Slf4j
public class MinioService {

    private final MinioClient minioClient;
    private final MinioConfig minioConfig;

    public String getPresignedUploadUrl(String filename, int expiryMinutes) {
        if (filename == null || filename.isBlank()) {
            throw new BaseException(DocExceptionType.EMPTY_FILE_ERROR);
        }

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
        if (filename == null || filename.isBlank()) {
            throw new BaseException(DocExceptionType.EMPTY_FILE_ERROR);
        }

        try {
            String presignedUrl = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(minioConfig.getBucketName())
                            .object(filename)
                            .expiry(expiryMinutes * 60)
                            .build()
            );

            String encodedFilename = URLEncoder.encode(filename, StandardCharsets.UTF_8)
                    .replaceAll("\\+", "%20");

            URI uri = new URI(presignedUrl);
            String newQuery = (uri.getQuery() == null ? "" : uri.getQuery() + "&") +
                    "response-content-disposition=attachment; filename=\"" + encodedFilename + "\"";

            URI updatedUri = new URI(
                    uri.getScheme(),
                    uri.getAuthority(),
                    uri.getPath(),
                    newQuery,
                    uri.getFragment()
            );

            String finalUrl = updatedUri.toString();
            log.info("Presigned Download URL 생성 성공: {}", finalUrl);
            return finalUrl;

        } catch (Exception e) {
            log.error("파일 다운로드 URL 생성 중 오류 발생: {}", e.getMessage(), e);
            throw new BaseException(DocExceptionType.FILE_DOWNLOAD_ERROR);
        }
    }

    public void deleteFile(String fileUrl) {
        if (fileUrl == null || fileUrl.isBlank()) {
            throw new BaseException(DocExceptionType.EMPTY_FILE_PATH_ERROR);
        }

        try {
            String objectName = extractObject(fileUrl);

            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(minioConfig.getBucketName())
                            .object(objectName)
                            .build()
            );

            log.info("파일 삭제 성공: {}", fileUrl);

        } catch (Exception e) {
            log.error("파일 삭제 중 오류 발생: {}", e.getMessage(), e);
            throw new BaseException(DocExceptionType.FILE_DELETE_ERROR);
        }
    }

    public String getFilePath(String filename) {
        if (filename == null || filename.isBlank()) {
            throw new BaseException(DocExceptionType.EMPTY_FILE_ERROR);
        }
        return "/" + minioConfig.getBucketName() + "/" + filename;
    }

    public boolean isExternalUrl(String url) {
        return url.startsWith("http://") || url.startsWith("https://");
    }

    public String extractObject(String fileUrl) {
        String bucketPrefix = "/" + minioConfig.getBucketName() + "/";
        if (!fileUrl.startsWith(bucketPrefix)) {
            throw new BaseException(DocExceptionType.INVALID_FILE_PATH_ERROR);
        }
        return fileUrl.replaceFirst("^" + bucketPrefix, "");
    }
}
