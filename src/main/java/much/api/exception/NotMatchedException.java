package much.api.exception;

import much.api.common.enums.Code;

public class NotMatchedException extends BusinessException {

    public NotMatchedException(Code code, String message) {
        super(message);
        this.code = code;
    }

}
