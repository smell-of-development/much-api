package much.api.common.exception;

import lombok.Getter;
import much.api.common.enums.Code;

@Getter
public class MuchException extends RuntimeException {

    private final Code code;

    public MuchException(Code code) {
        this.code = code;
    }

    public MuchException(Code code, String messageForLog) {
        super(messageForLog);
        this.code = code;
    }

    public MuchException(Code code, String messageForLog, Throwable cause) {
        super(messageForLog, cause);
        this.code = code;
    }
}
