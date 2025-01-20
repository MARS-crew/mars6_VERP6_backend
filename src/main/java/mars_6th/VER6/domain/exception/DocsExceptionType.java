package mars_6th.VER6.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import mars_6th.VER6.global.exception.ExceptionType;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum DocsExceptionType implements ExceptionType {

    NOT_FOUND(HttpStatus.NOT_FOUND, "docs-001", "문서를 찾을 수 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
