package mars_6th.VER6.domain.member.controller.dto.request;

import jakarta.annotation.Nullable;
import mars_6th.VER6.domain.member.entity.Member;
import mars_6th.VER6.domain.member.entity.MemberRole;

public record MemberRequest(
        String username,
        String password,
        @Nullable MemberRole role
) {
    public Member toEntity() {
        return Member.builder()
                .username(username)
                .password(password)
                .role(role)
                .build();
    }
}
