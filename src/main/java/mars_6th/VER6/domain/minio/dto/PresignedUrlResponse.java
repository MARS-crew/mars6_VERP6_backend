package mars_6th.VER6.domain.minio.dto;

public record PresignedUrlResponse(
        String presignedUrl,
        String generatedFileName
) {
    public static PresignedUrlResponse ofUpload(String presignedUrl, String generatedFileName) {
        return new PresignedUrlResponse(presignedUrl, generatedFileName);
    }

    public static PresignedUrlResponse ofDownload(String presignedUrl) {
        return new PresignedUrlResponse(presignedUrl, null);
    }
}
