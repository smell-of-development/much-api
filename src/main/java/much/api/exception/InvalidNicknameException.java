package much.api.exception;

import much.api.common.enums.Code;

public class InvalidNicknameException extends BusinessException {

    public InvalidNicknameException() {
        this.code = Code.INVALID_NICKNAME;
    }

}
