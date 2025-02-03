package mars_6th.VER6.domain.minio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PresignedUrlResponseDto {

    private String presignedUrl;
    private String generatedFileName;
}
