package much.api.common.exception;

import lombok.Getter;
import much.api.common.enums.Code;

@Getter
public class MuchException extends RuntimeException {

    private final Code code;

    public MuchException(Code code) {
        this.code = code;
    }

    public MuchException(Code code, String message) {
        super(message);
        this.code = code;
    }

    public MuchException(Code code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}
