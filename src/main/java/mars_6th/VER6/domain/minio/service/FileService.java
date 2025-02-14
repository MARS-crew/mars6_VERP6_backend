package mars_6th.VER6.domain.minio.service;

import lombok.RequiredArgsConstructor;
import mars_6th.VER6.domain.docs.entity.DocDetail;
import mars_6th.VER6.domain.docs.exception.DocExceptionType;
import mars_6th.VER6.domain.docs.repo.DocDetailRepository;
import mars_6th.VER6.domain.minio.dto.PresignedUrlRequest;
import mars_6th.VER6.domain.minio.dto.PresignedUrlResponse;
import mars_6th.VER6.global.exception.BaseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FileService {

    private final MinioService minioService;
    private final DocDetailRepository docDetailRepository;

    @Transactional
    public PresignedUrlResponse generatePresignedUploadUrl() {
        String generatedFileUrl = System.currentTimeMillis() + "_" + java.util.UUID.randomUUID().toString().substring(0, 5);

        String presignedUrl = minioService.getPresignedUploadUrl(generatedFileUrl, 10);
        return PresignedUrlResponse.ofUpload(presignedUrl, generatedFileUrl);
    }

    public PresignedUrlResponse getDownloadUrl(Long docDetailId, PresignedUrlRequest request) {
        DocDetail docDetail = docDetailRepository.getDocDetailById(docDetailId);

        if (docDetail.existExternalUrl()) {
            throw new BaseException(DocExceptionType.FILE_DOWNLOAD_ERROR);
        }

        String presignedUrl = minioService.getPresignedDownloadUrl(request.fileName(), docDetail.getUploadFileUrl(), 10);

        return PresignedUrlResponse.ofDownload(presignedUrl);
    }

    public void deleteFileIfInternal(String uploadFileUrl) {
        if (uploadFileUrl != null && !uploadFileUrl.isEmpty() && !minioService.isExternalUrl(uploadFileUrl)) {
            minioService.deleteFile(uploadFileUrl);
        }
    }
}