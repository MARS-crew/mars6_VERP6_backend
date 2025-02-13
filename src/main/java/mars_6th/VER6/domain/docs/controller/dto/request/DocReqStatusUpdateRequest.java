package mars_6th.VER6.domain.docs.controller.dto.request;

import mars_6th.VER6.domain.docs.entity.DocRequestStatus;

public record DocReqStatusUpdateRequest(
        Long reqId,
        DocRequestStatus status
) {
}
