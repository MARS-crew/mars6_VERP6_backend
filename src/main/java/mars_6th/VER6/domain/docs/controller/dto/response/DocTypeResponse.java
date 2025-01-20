package mars_6th.VER6.domain.docs.controller.dto.response;

import mars_6th.VER6.domain.docs.entity.DocType;

public record DocTypeResponse(
        Long id,
        String name
) {
    public static DocTypeResponse of(DocType docType) {
        return new DocTypeResponse(docType.getId(), docType.getName());
    }
}
