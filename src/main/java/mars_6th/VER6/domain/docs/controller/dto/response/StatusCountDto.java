package mars_6th.VER6.domain.docs.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import mars_6th.VER6.domain.docs.entity.DocRequestStatus;

@Data
@AllArgsConstructor
public class StatusCountDto {
    private DocRequestStatus status;
    private Long count;
}
