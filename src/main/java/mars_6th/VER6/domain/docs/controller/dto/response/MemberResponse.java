package mars_6th.VER6.domain.docs.controller.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberResponse {
    private String username;
    private String token;

    public MemberResponse(String username, String token) {
        this.username = username;
        this.token = token;
    }
}
