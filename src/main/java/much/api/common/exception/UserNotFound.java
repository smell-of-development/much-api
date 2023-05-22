package much.api.common.exception;

import static much.api.common.enums.Code.*;

public class UserNotFound extends BusinessException {

    public UserNotFound(String userId) {
        super(String.format("사용자 [%s] 를 찾지 못함", userId));
        this.code = USER_NOT_FOUND;
    }

}
