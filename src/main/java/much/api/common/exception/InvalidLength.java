package much.api.common.exception;

import static java.lang.String.format;
import static much.api.common.enums.Code.INVALID_LENGTH;

public class InvalidLength extends MuchException {

    public InvalidLength(String subject,
                         Integer minLength,
                         Integer maxLength,
                         Integer current) {

        super(INVALID_LENGTH, format(INVALID_LENGTH.getMessage(),
                subject,
                minLength,
                maxLength,
                current,
                maxLength)
        );
    }

}
