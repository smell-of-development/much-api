package much.api.common.exception;

import static java.lang.String.format;
import static much.api.common.enums.Code.MESSAGE_SENDING_FAIL;

public class MessageSendingFail extends MuchException {

    public MessageSendingFail(String phoneNumber) {
        super(MESSAGE_SENDING_FAIL, format("입력 휴대폰번호: [%s] - 메세지 전송 실패", phoneNumber));
    }

}
