package much.api.common.exception;

import static java.lang.String.format;
import static much.api.common.enums.Code.NEEDS_LESS_THAN_RECRUITED;

public class NeedsLessThanRecruited extends MuchException {

    public NeedsLessThanRecruited(int recruited) {
        super(NEEDS_LESS_THAN_RECRUITED, format(NEEDS_LESS_THAN_RECRUITED.getMessage(),
                recruited)
        );
    }

}
