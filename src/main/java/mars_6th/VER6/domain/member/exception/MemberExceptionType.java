package mars_6th.VER6.domain.member.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import mars_6th.VER6.global.exception.ExceptionType;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MemberExceptionType implements ExceptionType {

    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "member-001", "회원을 찾을 수 없습니다."),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "member-002", "아이디 또는 패스워드가 잘못되었습니다."),
    EMPTY_USER_INFO(HttpStatus.BAD_REQUEST, "member-003", "아이디 또는 패스워드에 잘못된 값이 있습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

}