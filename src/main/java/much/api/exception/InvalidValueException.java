package much.api.exception;

import much.api.common.enums.Code;

public class InvalidValueException extends BusinessException {

    public InvalidValueException(String target) {
        super(target);
        this.code = Code.INVALID_VALUE_FOR;
    }

}
