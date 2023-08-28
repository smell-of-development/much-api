package much.api.common.exception;

import static java.lang.String.*;
import static much.api.common.enums.Code.FILE_NOT_FOUND;

public class FileNotFound extends MuchException {

    public FileNotFound(String storedFilename) {
        super(FILE_NOT_FOUND, format("파일명: [%s] 을 찾을 수 없습니다.", storedFilename));
    }

}
