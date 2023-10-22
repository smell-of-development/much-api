package much.api.common.exception;

import static much.api.common.enums.Code.PROJECT_APPLICATION_NOT_FOUND;

public class ProjectApplicationNotFound extends MuchException {

    public ProjectApplicationNotFound() {
        super(PROJECT_APPLICATION_NOT_FOUND, PROJECT_APPLICATION_NOT_FOUND.getMessage());
    }

}
