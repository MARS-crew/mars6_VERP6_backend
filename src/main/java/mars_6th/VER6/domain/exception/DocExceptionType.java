package mars_6th.VER6.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import mars_6th.VER6.global.exception.ExceptionType;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum DocExceptionType implements ExceptionType {

    NOT_FOUND_DOC(HttpStatus.NOT_FOUND, "docs-001", "문서를 찾을 수 없습니다."),
    DUPLICATED_DOC(HttpStatus.BAD_REQUEST, "docs-002", "이미 존재하는 문서입니다."),

    NOT_FOUND_DOC_TYPE(HttpStatus.NOT_FOUND, "docs-type-001", "문서 타입을 찾을 수 없습니다."),
    DUPLICATED_DOC_TYPE(HttpStatus.BAD_REQUEST, "docs-type-002", "이미 존재하는 문서 타입입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
