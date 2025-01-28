package mars_6th.VER6.domain.docs.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import mars_6th.VER6.domain.docs.entity.DocRequestStatus;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class DocReqResponseDto {

    private String content;
    private String fileName;
    private DocRequestStatus status;
    private String name;
    private LocalDate createdAt;
}
