package much.api.common.exception;

import static java.lang.String.format;
import static much.api.common.enums.Code.FORBIDDEN;

public class NoAuthority extends MuchException {

    public NoAuthority(String target) {
        super(FORBIDDEN, format("[%s]에 대한 권한이 없습니다.", target));
    }

}
