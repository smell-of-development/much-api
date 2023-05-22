package much.api.common.exception;

import much.api.common.enums.Code;

public class CanNotRefreshTokenException extends BusinessException {

    public CanNotRefreshTokenException() {
        this.code = Code.CAN_NOT_REFRESH_TOKEN;
    }
}
