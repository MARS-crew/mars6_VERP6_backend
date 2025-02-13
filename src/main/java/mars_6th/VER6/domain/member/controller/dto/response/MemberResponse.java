package mars_6th.VER6.domain.member.controller.dto.response;

import mars_6th.VER6.domain.member.entity.Member;
import mars_6th.VER6.domain.member.entity.MemberRole;

public record MemberResponse(
        String username,
        String name,
        MemberRole role,
        String token
) {
    public static MemberResponse from(Member member, String token) {
        return new MemberResponse(member.getUsername(), member.getName(),member.getRole(), token);
    }
}
