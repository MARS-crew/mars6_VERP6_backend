package mars_6th.VER6.domain.docs.controller.dto.response;

import mars_6th.VER6.domain.docs.entity.Doc;
import mars_6th.VER6.global.utils.TimeUtils;

public record DocResponse(
        Long docId,
        String title,
        String fileName,
        Long completedRequestStep,
        Long inProgressRequestStep,
        Long pendingRequestStep,
        String timeAgo
) {
    public static DocResponse of(Doc doc, String fileName, Long completedRequestStep,
                                 Long inProgressRequestStep, Long canceledRequestStep) {
        return new DocResponse(
                doc.getId(),
                doc.getTitle(),
                fileName,
                completedRequestStep,
                inProgressRequestStep,
                canceledRequestStep,
                TimeUtils.timeAgo(doc.getCreatedAt()));
    }
}

