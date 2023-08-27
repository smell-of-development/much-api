package much.api.exception;

import static much.api.common.enums.Code.FILE_PROCESS_ERROR;

public class FileProcessError extends MuchException {

    public FileProcessError(String messageForLog) {
        super(FILE_PROCESS_ERROR, messageForLog);
    }

    public FileProcessError(String messageForLog, Throwable cause) {
        super(FILE_PROCESS_ERROR, messageForLog, cause);
    }

}
