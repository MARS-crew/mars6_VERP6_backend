package mars_6th.VER6.domain.docs.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import mars_6th.VER6.global.exception.ExceptionType;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Getter
@AllArgsConstructor
public enum DocExceptionType implements ExceptionType {

    NOT_FOUND_DOC(HttpStatus.NOT_FOUND, "docs-001", "문서를 찾을 수 없습니다."),
    DUPLICATED_DOC(HttpStatus.BAD_REQUEST, "docs-002", "이미 존재하는 문서입니다."),

    NOT_FOUND_DOC_TYPE(HttpStatus.NOT_FOUND, "docs-type-001", "문서 타입을 찾을 수 없습니다."),
    DUPLICATED_DOC_TYPE(HttpStatus.BAD_REQUEST, "docs-type-002", "이미 존재하는 문서 타입입니다."),

    DOC_NOT_FOUND(NOT_FOUND, "docs-request-001", "요청한 문서를 찾을 수 없습니다."),
    FILE_UPLOAD_ERROR(BAD_REQUEST, "docs-request-002", "파일 업로드 중 오류가 발생했습니다."),
    FILE_DOWNLOAD_ERROR(BAD_REQUEST, "docs-request-003", "파일 다운로드 중 오류가 발생했습니다."), // 추가된 항목
    FILE_DELETE_ERROR(BAD_REQUEST, "docs-request-004", "파일 삭제 중 오류가 발생했습니다."),
    EMPTY_FILE_ERROR(BAD_REQUEST, "docs-request-005", "업로드할 파일이 비어 있습니다."),
    EMPTY_FILE_PATH_ERROR(BAD_REQUEST, "docs-request-006", "삭제할 파일 경로가 비어 있습니다."),
    INVALID_FILE_PATH_ERROR(BAD_REQUEST, "docs-request-007", "잘못된 파일 경로입니다.")
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

}
