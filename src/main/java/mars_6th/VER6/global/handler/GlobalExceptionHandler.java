package mars_6th.VER6.global.handler;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import mars_6th.VER6.global.exception.BaseException;
import mars_6th.VER6.global.exception.BaseExceptionType;
import mars_6th.VER6.global.exception.ExceptionType;
import mars_6th.VER6.global.response.ExceptionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.PrintWriter;
import java.io.StringWriter;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    ResponseEntity<ExceptionResponse> handleException(HttpServletRequest request, Exception e) {
        log.error("예상하지 못한 예외가 발생했습니다. URI: {}, 내용: {}", request.getRequestURI(), convertToString(e));
        return ResponseEntity.internalServerError()
                .body(new ExceptionResponse(BaseExceptionType.UNKNOWN_SERVER_ERROR));
    }

    @ExceptionHandler(BaseException.class)
    ResponseEntity<ExceptionResponse> handleBaseException(HttpServletRequest request, BaseException e) {
        ExceptionType exceptionType = e.exceptionType();
        log.warn("{} URI: {}", exceptionType.getMessage(), request.getRequestURI());
        return ResponseEntity.status(exceptionType.getHttpStatus())
                .body(new ExceptionResponse(exceptionType));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(HttpServletRequest request, MethodArgumentNotValidException e) {
        log.warn("요청 인자가 올바르지 않습니다 URI: {}", request.getRequestURI());
        return ResponseEntity.badRequest()
                .body(new ExceptionResponse(BaseExceptionType.ARGUMENT_NOT_VALID));
    }

    private String convertToString(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }
}
