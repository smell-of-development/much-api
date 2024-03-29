package much.api.common.exception;

import static java.lang.String.format;
import static much.api.common.enums.Code.NOT_IMAGE_FILE;

public class NotImageFile extends MuchException {

    public NotImageFile(String contentType) {
        super(NOT_IMAGE_FILE, format("Content Type : [%s] - 이미지 형식이 아님", contentType));
    }

}
