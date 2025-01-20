package mars_6th.VER6.domain.docs.controller.dto.request;

import jakarta.validation.constraints.Pattern;
import mars_6th.VER6.domain.docs.entity.DocType;
import mars_6th.VER6.global.validator.NoSpaceSize;

public record DocTypeCreateRequest(
        @NoSpaceSize(min = 1, max = 8, message = "공백을 제외한 글자 수는 1자 이상 8자 이하여야 합니다.")
        @Pattern(
                regexp = "^[가-힣a-zA-Z0-9\\s]+$",
                message = "제목은 한글, 영어, 숫자, 공백만 허용됩니다."
        )
        String title
) {
    public DocType toEntity(Long memberId) {
        return DocType.builder()
                .name(title)
                .createdBy(memberId)
                .build();
    }
}