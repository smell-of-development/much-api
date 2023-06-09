package much.api.exception;

import lombok.Getter;
import much.api.common.enums.Code;

@Getter
public class BusinessException extends RuntimeException {

    private final Code code;

    public BusinessException(Code code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(Code code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}
