package much.api.common.exception;

import much.api.common.enums.Code;

public class DuplicatedNicknameException extends BusinessException {

    public DuplicatedNicknameException() {
        this.code = Code.DUPLICATED_NICKNAME;
    }

}
