package mars_6th.VER6.domain.docs.controller.dto.response;

import mars_6th.VER6.domain.docs.entity.DocRequest;
import mars_6th.VER6.domain.docs.entity.DocRequestStatus;

import java.time.LocalDate;

public record DocReqResponse(
        Long reqId,
        String title,
        String content,
        String assignee,
        DocRequestStatus status,
        LocalDate createdAt
) {
    public static DocReqResponse of(DocRequest docRequest) {
        return new DocReqResponse(
                docRequest.getId(),
                docRequest.getTitle(),
                docRequest.getContent(),
                docRequest.getAssignee(),
                docRequest.getStatus(),
                docRequest.getCreatedAt().toLocalDate()
        );
    }
}
