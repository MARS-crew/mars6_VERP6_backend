package mars_6th.VER6.domain.docs.controller.dto.response;

import mars_6th.VER6.domain.docs.entity.Doc;
import mars_6th.VER6.global.utils.TimeUtils;

public record DocResponse(
        Long docId,
        String title,
        String fileName,
        Long currentRequestStep,
        Long totalRequestStep,
        Double completion,
        String timeAgo
) {
    public static DocResponse of(Doc doc, Long currentStep, Long totalStep) {
        Double completion = totalStep != 0 ? (double) currentStep / totalStep * 100 : 0.0;
        String timeAgo = TimeUtils.timeAgo(doc.getCreatedAt());

        return new DocResponse(doc.getId(), doc.getTitle(), doc.getFileName(), currentStep, totalStep, completion, timeAgo);
    }
}
