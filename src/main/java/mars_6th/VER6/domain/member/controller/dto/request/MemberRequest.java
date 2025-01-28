package mars_6th.VER6.domain.member.controller.dto.request;

import lombok.Getter;
import lombok.Setter;
import mars_6th.VER6.domain.member.entity.Member;

public record MemberRequest(
        String username,
        String password
){
    public Member toEntity() {
        return Member.builder()
                .username(username)
                .password(password)
                .build();
    }
}
