package mars_6th.VER6.domain.docs.controller.dto.request;

import mars_6th.VER6.domain.docs.entity.DocRequest;
import mars_6th.VER6.domain.docs.entity.DocRequestStatus;

public record DocReqRequest(
        String title,
        String content,
        String assignee
) {
    public DocRequest toEntity() {
        return DocRequest.builder()
                .title(title)
                .content(content)
                .assignee(assignee)
                .status(DocRequestStatus.PENDING)
                .build();
    }
}
