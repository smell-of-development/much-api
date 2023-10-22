package much.api.common.exception;

import static java.lang.String.format;
import static much.api.common.enums.Code.PROJECT_POSITION_NOT_FOUND;

public class ProjectPositionNotFound extends MuchException {

    public ProjectPositionNotFound(Long id) {
        super(PROJECT_POSITION_NOT_FOUND,
                format("프로젝트 포지션 ID: [%s] - 미존재", id));
    }

}
