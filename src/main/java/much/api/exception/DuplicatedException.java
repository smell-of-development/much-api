package much.api.exception;

import much.api.common.enums.Code;

public class DuplicatedException extends BusinessException {

    public DuplicatedException(Code code, String message) {
        super(message);
        this.code = code;
    }

}
