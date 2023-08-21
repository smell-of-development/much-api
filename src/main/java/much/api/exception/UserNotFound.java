package much.api.exception;

import static java.lang.String.format;
import static much.api.common.enums.Code.USER_NOT_FOUND;

public class UserNotFound extends MuchException {

    public UserNotFound(Long id) {
        super(USER_NOT_FOUND, format("사용자 PK: [%s] - 미존재", id));
    }

}
