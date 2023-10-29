package much.api.common.exception;

import static java.lang.String.*;
import static much.api.common.enums.Code.NEEDS_LESS_THAN_RECRUITED_POSITION;

public class NeedsLessThanRecruitedPosition extends MuchException {

    public NeedsLessThanRecruitedPosition(String position, int recruited) {
        super(NEEDS_LESS_THAN_RECRUITED_POSITION, format(NEEDS_LESS_THAN_RECRUITED_POSITION.getMessage(),
                position,
                recruited)
        );
    }

}
