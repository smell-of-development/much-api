package much.api.exception;

import much.api.common.enums.Code;

public class MessageSendingFailException extends BusinessException {

    public MessageSendingFailException(String message) {
        super(message);
    }

    public MessageSendingFailException(String message, Throwable cause) {
        super(message, cause);
        this.code = Code.MESSAGE_SENDING_FAIL;
    }
}
