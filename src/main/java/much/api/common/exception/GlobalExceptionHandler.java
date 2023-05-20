package much.api.common.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import much.api.dto.response.Envelope;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import static much.api.common.enums.Code.*;


@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    /**
     * validation.Valid or @Validated binding error 발생시
     */
    @ExceptionHandler(BindException.class)
    protected ResponseEntity<Envelope<Void>> handleBindException(BindException e) {
        log.error("handleBindException", e);

        Envelope<Void> response = Envelope.error(
                INVALID_INPUT_VALUE,
                e.getBindingResult(),
                messageSource
        );

        return ResponseEntity.ok(response);
    }


    /**
     * enum type 일치하지 않아 binding 못할 경우
     * 주로 @RequestParam enum 으로 binding 못했을 경우 발생
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Envelope<Void>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error("handleMethodArgumentTypeMismatchException", e);

        Envelope<Void> response = Envelope.error(METHOD_NOT_ALLOWED);

        return ResponseEntity.ok(response);
    }


    /**
     * 지원하지 않는 HTTP method 호출 할 경우
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<Envelope<Void>> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("handleHttpRequestMethodNotSupportedException", e);

        Envelope<Void> response = Envelope.error(METHOD_NOT_ALLOWED);

        return ResponseEntity.ok(response);
    }


    /**
     * 인증 실패
     */
    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<Envelope<Void>> handleAccessDeniedException(AuthenticationException e) {
        log.error("handleAuthenticationException", e);

        Envelope<Void> response = Envelope.error(UNAUTHORIZED);

        return ResponseEntity.ok(response);
    }


    /**
     * 필요한 권한이 없는 경우
     */
    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<Envelope<Void>> handleAccessDeniedException(AccessDeniedException e) {
        log.error("handleAccessDeniedException", e);

        Envelope<Void> response = Envelope.error(FORBIDDEN);

        return ResponseEntity.ok(response);
    }


    /**
     * 파일 업로드 사이즈 초과
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    protected ResponseEntity<Envelope<Void>> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        log.error("handleMaxUploadSizeExceededException", e);

        Envelope<Void> response = Envelope.error(FILE_UPLOAD_SIZE_EXCEEDED);

        return ResponseEntity.ok(response);
    }


    /**
     * JSON 요청 형태가 아닌 경우
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<Envelope<Void>> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("handleHttpMessageNotReadableException", e);

        Envelope<Void> response = Envelope.error(INCORRECT_FORMAT);

        return ResponseEntity.ok(response);
    }


    /**
     * 처리 핸들러를 찾지 못한경우
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    protected ResponseEntity<Envelope<Void>> handleNoHandlerFoundException(NoHandlerFoundException e) {
        log.error("handleNoHandlerFoundException", e);

        Envelope<Void> response = Envelope.error(HANDLER_NOT_FOUND);

        return ResponseEntity.ok(response);
    }


    /**
     * 비즈니스 예외
     */
    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<Envelope<Void>> handleBusinessException(BusinessException e) {
        log.error("handleBusinessException", e);

        Envelope<Void> response = Envelope.error(e.getCode(), e.getArgs());

        return ResponseEntity.ok(response);
    }


    /**
     * 나머지 모든 예외
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Envelope<Void>> handleException(Exception e) {
        log.error("handleException", e);

        Envelope<Void> response = Envelope.error(INTERNAL_SERVER_ERROR);

        return ResponseEntity.internalServerError().body(response);
    }

}
