package much.api.common.exception;

import static much.api.common.enums.Code.PICK_PROCESSING_FAIL;

public class PickProcessingFail extends MuchException {

    public PickProcessingFail(String message) {
        super(PICK_PROCESSING_FAIL, message);
    }

}
