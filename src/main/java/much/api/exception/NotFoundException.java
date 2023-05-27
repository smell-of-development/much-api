package much.api.exception;

import much.api.common.enums.Code;

public class NotFoundException extends BusinessException {

    public NotFoundException(Code code, String message) {
        super(message);
        this.code = code;
    }

}
