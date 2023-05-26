package much.api.exception;

import much.api.common.enums.Code;

public class TokenRefreshBlockedUserException extends BusinessException {

    public TokenRefreshBlockedUserException(String userId) {
        super(String.format("토큰 리프레시가 차단된 유저[%s]", userId));
        this.code = Code.TOKEN_REFRESH_BLOCKED_USER;
    }

}
