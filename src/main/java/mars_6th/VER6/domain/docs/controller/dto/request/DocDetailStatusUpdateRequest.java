package mars_6th.VER6.domain.docs.controller.dto.request;

import mars_6th.VER6.domain.docs.entity.DocDetailStatus;

public record DocDetailStatusUpdateRequest(
        Long docDetailId,
        DocDetailStatus status
) {
}
