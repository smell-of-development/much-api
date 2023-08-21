package much.api.exception;

import static java.lang.String.format;
import static much.api.common.enums.Code.PROJECT_NOT_FOUND;

public class ProjectNotFound extends MuchException {

    public ProjectNotFound(Long id) {
        super(PROJECT_NOT_FOUND, format("프로젝트 ID: [%s] - 미존재", id));
    }

}
