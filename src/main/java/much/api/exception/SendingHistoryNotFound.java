package much.api.exception;

import static java.lang.String.format;
import static much.api.common.enums.Code.SENDING_HISTORY_NOT_FOUND;

public class SendingHistoryNotFound extends MuchException {

    public SendingHistoryNotFound(String phoneNumber) {
        super(SENDING_HISTORY_NOT_FOUND, format("입력 휴대폰번호: [%s] - 전송기록 미존재", phoneNumber));
    }

}
