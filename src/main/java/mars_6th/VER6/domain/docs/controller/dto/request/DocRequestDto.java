package mars_6th.VER6.domain.docs.controller.dto.request;

import mars_6th.VER6.domain.docs.entity.Doc;
import mars_6th.VER6.global.validator.NoSpaceSize;

public record DocRequestDto(
        @NoSpaceSize(min = 1, max = 20, message = "공백을 제외한 글자 수는 1자 이상 20자 이하여야 합니다.")
        String title,
        Long docTypeId
) {
    public Doc toEntity() {
        return Doc.builder()
                .title(title)
                .build();
    }
}
