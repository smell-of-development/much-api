package much.api.common.exception;

import static much.api.common.enums.Code.*;

public class UserNotFound extends BusinessException {

    public UserNotFound() {
        this.code = USER_NOT_FOUND;
    }

}
