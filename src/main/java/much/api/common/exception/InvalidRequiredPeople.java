package much.api.common.exception;

import static java.lang.String.format;
import static much.api.common.enums.Code.INVALID_REQUIRED_PEOPLE;

public class InvalidRequiredPeople extends MuchException {

    public InvalidRequiredPeople(Integer input) {
        super(INVALID_REQUIRED_PEOPLE, format("모집 인원수 이상 [%s]", input));
    }

}
