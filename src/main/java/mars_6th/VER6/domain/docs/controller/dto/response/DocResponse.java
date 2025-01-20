package mars_6th.VER6.domain.docs.controller.dto.response;

import mars_6th.VER6.domain.docs.entity.Doc;
import mars_6th.VER6.global.utils.TimeUtils;

public record DocResponse(
        String title,
        String fileName,
        Long currentStep,
        Long totalStep,
        Double completion,
        String timeAgo
) {
    public static DocResponse of(Doc doc, Long currentStep, Long totalStep) {
        Double completion = (double) currentStep / totalStep * 100;
        String timeAgo = TimeUtils.timeAgo(doc.getCreatedAt());

        return new DocResponse(doc.getTitle(), doc.getFileName(), currentStep, totalStep, completion, timeAgo);
    }
}
