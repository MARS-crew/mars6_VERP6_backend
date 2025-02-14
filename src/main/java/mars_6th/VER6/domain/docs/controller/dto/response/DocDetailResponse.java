package mars_6th.VER6.domain.docs.controller.dto.response;

import mars_6th.VER6.domain.docs.entity.DocDetail;
import mars_6th.VER6.domain.docs.entity.DocDetailStatus;

import java.time.LocalDate;

public record DocDetailResponse(
        Long docDetailId,
        String version,
        String fileName,
        String content,
        LocalDate createdAt,
        DocDetailStatus status
) {
    public static DocDetailResponse of(DocDetail docDetail) {
        return new DocDetailResponse(
                docDetail.getId(),
                docDetail.getVersion(),
                docDetail.getOriginalFileName(),
                docDetail.getContent(),
                docDetail.getCreatedAt().toLocalDate(),
                docDetail.getStatus()
        );
    }
}
