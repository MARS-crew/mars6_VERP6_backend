package mars_6th.VER6.domain.docs.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class DeResponseDto {

    private Long docId;
    private String version;
    private String fileName;
    private String content;
    private LocalDate createdAt;
}
