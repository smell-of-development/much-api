package much.api.common.exception;

import static java.lang.String.format;
import static much.api.common.enums.Code.ALREADY_RECRUITED_POSITION;

public class AlreadyRecruitedPosition extends MuchException {

    public AlreadyRecruitedPosition(String position) {
        super(ALREADY_RECRUITED_POSITION,
                format(ALREADY_RECRUITED_POSITION.getMessage(), position));
    }

}
