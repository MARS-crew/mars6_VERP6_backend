package mars_6th.VER6.domain.docs.service;

import io.minio.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mars_6th.VER6.global.config.MinioConfig;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class MinioService {

    private final MinioClient minioClient;
    private final MinioConfig minioConfig;

    public String uploadFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("업로드할 파일이 비어 있습니다.");
        }

        try {
            String originalFilename = file.getOriginalFilename();
            String uniqueFileName = System.currentTimeMillis() + "_" + originalFilename;

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(minioConfig.getBucketName())
                            .object(uniqueFileName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );

            String fileUrl = "/" + minioConfig.getBucketName() + "/" + uniqueFileName;
            log.info("파일 업로드 성공: {}", fileUrl);

            return fileUrl;
        } catch (Exception e) {
            log.error("파일 업로드 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("파일 업로드 중 오류 발생", e);
        }
    }

    public InputStream downloadFile(String filename) {
        if (filename == null || filename.isBlank()) {
            throw new IllegalArgumentException("파일 이름이 비어 있습니다.");
        }

        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(minioConfig.getBucketName())
                            .object(filename)
                            .build()
            );
        } catch (Exception e) {
            log.error("파일 다운로드 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("파일 다운로드 중 오류 발생", e);
        }
    }

    public void deleteFile(String fileUrl) {
        if (fileUrl == null || fileUrl.isBlank()) {
            throw new IllegalArgumentException("삭제할 파일 경로가 비어 있습니다.");
        }

        try {
            String objectName = fileUrl.startsWith("/") ? fileUrl.substring(1) : fileUrl;

            log.info("삭제하려는 파일 경로 (최종 경로): {}", objectName);
            log.info("파일 삭제 요청: Bucket: {}, Object: {}", minioConfig.getBucketName(), objectName);
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(minioConfig.getBucketName())
                            .object(objectName)
                            .build()
            );

            try {
                minioClient.statObject(
                        StatObjectArgs.builder()
                                .bucket(minioConfig.getBucketName())
                                .object(objectName)
                                .build()
                );
            } catch (Exception e) {
                log.info("파일 삭제 확인됨: {}", objectName);
            }

            log.info("파일 삭제 성공: {}", fileUrl);
        } catch (Exception e) {
            log.error("파일 삭제 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("파일 삭제 중 오류 발생", e);
        }
    }

    public boolean isExternalUrl(String url) {
        return url.startsWith("http://") || url.startsWith("https://");
    }
}