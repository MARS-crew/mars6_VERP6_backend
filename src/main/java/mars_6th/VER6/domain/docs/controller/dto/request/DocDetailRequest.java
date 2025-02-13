package mars_6th.VER6.domain.docs.controller.dto.request;

import mars_6th.VER6.domain.docs.entity.DocDetail;

public record DocDetailRequest(
        String content,
        String version
) {
    public DocDetail toEntity() {
        return DocDetail.builder()
                .content(content)
                .version(version)
                .build();
    }
}
