package mars_6th.VER6.domain.member.controller.dto.response;

import lombok.Getter;
import lombok.Setter;
import mars_6th.VER6.domain.member.entity.Member;

@Getter
@Setter
public record MemberResponse(
        String username,
        String token
) {
    public static MemberResponse from(Member member, String token) {
        return new MemberResponse(member.getUsername(), token);
    }
}
