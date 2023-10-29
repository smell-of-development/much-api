package much.api.common.exception;

import static java.lang.String.format;
import static much.api.common.enums.Code.PROJECT_NOT_FOUND;

public class StudyNotFound extends MuchException {

    public StudyNotFound(Long id) {
        super(PROJECT_NOT_FOUND, format("스터디 ID: [%s] - 미존재", id));
    }

}
