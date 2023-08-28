package much.api.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import much.api.dto.response.Envelope;
import much.api.common.exception.MuchException;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
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
     * => 기본제공 기능은 컨트롤러에서 개발용으로 사용.
     * 사용자 입력은 커스텀 Validator 사용하여 위에서부터 하나씩 검증
     */
    @ExceptionHandler({BindException.class, MethodArgumentNotValidException.class})
    protected ResponseEntity<Envelope<Void>> handleBindException(BindException e) {
        log.error("handleBindException", e);

        Envelope<Void> response = Envelope.error(
                DEV_INVALID_VALUE,
                e.getBindingResult(),
                messageSource
        );

        return ResponseEntity.ok(response);
    }


    /**
     * @RequestParam 값 미존재로 바인딩 실패시
     */
    @ExceptionHandler(ServletRequestBindingException.class)
    protected ResponseEntity<Envelope<Void>> handleServletRequestBindingException(ServletRequestBindingException e) {
        log.error("bindingException", e);

        Envelope<Void> response = Envelope.error(DEV_INVALID_PARAM_NAME);

        return ResponseEntity.ok(response);
    }


    /**
     * @RequestParam 타입 바인딩 실패시
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Envelope<Void>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error("bindingException", e);

        Envelope<Void> response = Envelope.error(DEV_INVALID_PARAM_VALUE);

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

        Envelope<Void> response = Envelope.error(DEV_INCORRECT_FORMAT);

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
     * 서비스 예외
     */
    @ExceptionHandler(MuchException.class)
    protected ResponseEntity<Envelope<Void>> handleBusinessException(MuchException e) {
        log.error("handleBusinessException", e);

        Envelope<Void> response = Envelope.error(e.getCode(), e.getMessage());

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
