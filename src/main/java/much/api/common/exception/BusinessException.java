package much.api.common.exception;

import lombok.Getter;
import much.api.common.enums.Code;

@Getter
public abstract class BusinessException extends RuntimeException {

    protected Code code;

    public BusinessException() {
    }

    public BusinessException(String message) {
        super(message);
    }

}
