package mars_6th.VER6.domain.docs.controller.dto.response;

import mars_6th.VER6.domain.docs.entity.DocDetailRejectReason;

public record DocDetailRejectReasonResponse(
        Long rejectId,
        String reason
) {
    public static DocDetailRejectReasonResponse of(DocDetailRejectReason rejectReason) {
        return new DocDetailRejectReasonResponse(
                rejectReason.getId(),
                rejectReason.getReason()
        );
    }
}
