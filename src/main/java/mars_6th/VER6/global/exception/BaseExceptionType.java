package mars_6th.VER6.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum BaseExceptionType implements ExceptionType {

    TEST_FAIL(BAD_REQUEST, "-1", "테스트용 예외가 발생했습니다."),
    UNKNOWN_SERVER_ERROR(INTERNAL_SERVER_ERROR, "5000", "서버가 응답할 수 없습니다."),
    ARGUMENT_NOT_VALID(BAD_REQUEST, "4001", "요청 인자가 잘못되었습니다."),
    NOT_VALID_METHODS(METHOD_NOT_ALLOWED, "4001", "지원하지 않는 메서드입니다."),
    DOC_NOT_FOUND(NOT_FOUND, "4041", "요청한 문서를 찾을 수 없습니다."),
    FILE_UPLOAD_ERROR(BAD_REQUEST, "4003", "파일 업로드 중 오류가 발생했습니다."),
    FILE_DOWNLOAD_ERROR(BAD_REQUEST, "4004", "파일 다운로드 중 오류가 발생했습니다."), // 추가된 항목
    FILE_DELETE_ERROR(BAD_REQUEST, "4005", "파일 삭제 중 오류가 발생했습니다."),
    EMPTY_FILE_ERROR(BAD_REQUEST, "4006", "업로드할 파일이 비어 있습니다."),
    EMPTY_FILE_PATH_ERROR(BAD_REQUEST, "4007", "삭제할 파일 경로가 비어 있습니다."),
    INVALID_FILE_PATH_ERROR(BAD_REQUEST, "4008", "잘못된 파일 경로입니다.")
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

}