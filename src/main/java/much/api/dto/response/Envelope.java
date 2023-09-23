package much.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import much.api.common.enums.Code;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static much.api.common.enums.Code.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Envelope<R> {

    @Schema(defaultValue = "200", example = "200")
    private Integer code;

    @Schema(description = "메세지")
    private String message;

    @Schema(description = "(개발용)필수값 누락 확인")
    private List<Error> requires;

    private R result;

    private Envelope(final Integer code,
                     final R result) {

        this.code = code;
        this.result = result;
    }

    private Envelope(final Code code) {

        this.code = code.getCode();
        this.message = code.getMessage();
    }

    private Envelope(final int code) {

        this.code = code;
    }

    private Envelope(final String message) {

        this.code = 2000;
        this.message = message;
    }

    private Envelope(final Code code,
                     final String message) {

        this.code = code.getCode();
        this.message = String.format(code.getMessage(), message);
    }

    private Envelope(final Code code,
                     final List<Error> errors) {

        this.code = code.getCode();
        this.message = code.getMessage();
        this.requires = errors;
    }

    private Envelope(final Code code,
                     final R result) {

        this.code = code.getCode();
        this.message = code.getMessage();
        this.result = result;
    }


    public static <R> Envelope<R> ok(final R data) {

        return new Envelope<>(HttpStatus.OK.value(), data);
    }


    public static <R> Envelope<R> okWithCode(final Code code,
                                             final R data) {

        return new Envelope<>(code, data);
    }


    public static Envelope<Void> empty() {

        return new Envelope<>(200);
    }


    public static Envelope<Void> error(final Code code,
                                       final String... message) {

        if (code == null) {
            return new Envelope<>(message[0]);
        }
        if (code == DEV_MESSAGE) {
            return new Envelope<>(code, message[0]);
        }

        return new Envelope<>(code);
    }


    public static Envelope<Void> error(final Code code,
                                       final BindingResult bindingResult,
                                       final MessageSource messageSource) {

        return new Envelope<>(code, Error.of(bindingResult, messageSource));
    }


    private record Error(String target, String value, String reason) {

        public static List<Error> of(final String target,
                                     final String value,
                                     final String reason) {


            List<Error> errors = new ArrayList<>();
            errors.add(new Error(target, value, reason));
            return errors;
        }


        private static List<Error> of(final BindingResult bindingResult,
                                      final MessageSource messageSource) {

            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            if (!fieldErrors.isEmpty()) {
                return fieldErrors.stream()
                        .map(fieldError -> new Error(
                                fieldError.getField(),
                                fieldError.getRejectedValue() == null ? "" : fieldError.getRejectedValue().toString(),
                                messageSource.getMessage(fieldError, Locale.KOREA)))
                        .collect(Collectors.toList());
            }

            List<ObjectError> allErrors = bindingResult.getAllErrors();
            return allErrors.stream()
                    .map(objectError -> new Error(
                            objectError.getObjectName(),
                            "",
                            objectError.getDefaultMessage()))
                    .collect(Collectors.toList());
        }
    }

}
