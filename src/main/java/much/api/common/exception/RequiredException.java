package much.api.common.exception;

import much.api.common.enums.Code;

public class RequiredException extends BusinessException {

    public RequiredException() {
        this.code = Code.REQUIRED_INFORMATION;
    }

}
