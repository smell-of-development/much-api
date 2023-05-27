package much.api.exception;

import much.api.common.enums.Code;

public class NotSupportedException extends BusinessException {

    public NotSupportedException(Code code, String message) {
        super(message);
        this.code = code;
    }

}
