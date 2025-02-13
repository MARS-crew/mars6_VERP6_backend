package mars_6th.VER6.domain.minio.service;

import lombok.RequiredArgsConstructor;
import mars_6th.VER6.domain.docs.entity.DocDetail;
import mars_6th.VER6.domain.docs.exception.DocExceptionType;
import mars_6th.VER6.domain.docs.repo.DocDetailRepository;
import mars_6th.VER6.domain.minio.dto.PresignedUrlResponse;
import mars_6th.VER6.global.exception.BaseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FileService {

    private final MinioService minioService;
    private final RedisFileService redisFileService;
    private final DocDetailRepository docDetailRepository;

    @Transactional
    public PresignedUrlResponse generatePresignedUploadUrl(Long userId) {
        String generatedFileName = System.currentTimeMillis() + "_" + java.util.UUID.randomUUID();
        redisFileService.saveFileName(userId, generatedFileName);

        String presignedUrl = minioService.getPresignedUploadUrl(generatedFileName, 10);
        return PresignedUrlResponse.ofUpload(presignedUrl, generatedFileName);
    }

    /**
     * 외부 URL이 주어지면 그대로 반환하고, 없으면 redis에 저장된 파일명을 이용하여 minio에 저장된 파일 URL을 반환한다.
     */
    public String extractFileUrl(Long userId) {
        String fileName = redisFileService.getFileName(userId);

        if (fileName == null) {
            throw new BaseException(DocExceptionType.EMPTY_FILE_ERROR);
        }

        return fileName;
    }

    public PresignedUrlResponse getDownloadUrl(Long docDetailId) {
        DocDetail docDetail = docDetailRepository.getDocDetailById(docDetailId);

        if (docDetail.existExternalUrl()) {
            throw new BaseException(DocExceptionType.FILE_DOWNLOAD_ERROR);
        }

        String presignedUrl = minioService.getPresignedDownloadUrl(docDetail.getFileName(), 10);

        return PresignedUrlResponse.ofDownload(presignedUrl);
    }

    public void deleteFileIfInternal(String fileName) {
        if (fileName != null && !fileName.isEmpty() && !minioService.isExternalUrl(fileName)) {
            minioService.deleteFile(fileName);
        }
    }
}