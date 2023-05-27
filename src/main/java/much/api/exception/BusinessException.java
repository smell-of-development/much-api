package much.api.exception;

import lombok.Getter;
import much.api.common.enums.Code;

@Getter
public abstract class BusinessException extends RuntimeException {

    protected Code code;

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
