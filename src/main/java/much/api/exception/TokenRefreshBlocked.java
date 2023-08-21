package much.api.exception;

import static java.lang.String.format;
import static much.api.common.enums.Code.TOKEN_REFRESH_BLOCKED;

public class TokenRefreshBlocked extends MuchException {

    public TokenRefreshBlocked(Long id) {
        super(TOKEN_REFRESH_BLOCKED, format("ID: [%s] - 토큰 재발급 불가", id));
    }

}
