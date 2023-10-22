package much.api.common.exception;

import static much.api.common.enums.Code.APPLICATION_FORM_NOT_FOUND;

public class ApplicationFormNotFound extends MuchException {

    public ApplicationFormNotFound() {
        super(APPLICATION_FORM_NOT_FOUND, APPLICATION_FORM_NOT_FOUND.getMessage());
    }

}
