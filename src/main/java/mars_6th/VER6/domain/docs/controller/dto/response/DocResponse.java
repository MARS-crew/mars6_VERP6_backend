package mars_6th.VER6.domain.docs.controller.dto.response;

import mars_6th.VER6.domain.docs.entity.Doc;
import mars_6th.VER6.global.utils.TimeUtils;

public record DocResponse(
        Long docId,
        String title,
        String fileName,
        Long completedRequestStep,
        Long inProgressRequestStep,
        Long canceledRequestStep,
        Long totalRequestStep,
        String timeAgo
) {
    public static DocResponse of(Doc doc, Long completedRequestStep,
                                 Long inProgressRequestStep, Long canceledRequestStep,
                                 Long totalRequestStep) {
        String timeAgo = TimeUtils.timeAgo(doc.getCreatedAt());

        return new DocResponse(
                doc.getId(),
                doc.getTitle(),
                doc.getFileName(),
                completedRequestStep,
                inProgressRequestStep,
                canceledRequestStep,
                totalRequestStep,
                timeAgo);
    }
}
