package much.api.common.exception;

import lombok.Getter;
import much.api.common.enums.Code;

@Getter
public abstract class BusinessException extends RuntimeException {

    protected Code code;

    protected Object[] args;

    public BusinessException() {
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

}
