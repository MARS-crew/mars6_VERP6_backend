package mars_6th.VER6.domain.docs.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class DeResponseDto {

    private String version;
    private String fileName;
    private LocalDate createdAt;
}
