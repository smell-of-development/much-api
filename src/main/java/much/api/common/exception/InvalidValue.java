package much.api.common.exception;

import much.api.common.enums.Code;

public class InvalidValue extends MuchException {

    public InvalidValue(Code code) {
        super(code, code.getMessage());
    }

}