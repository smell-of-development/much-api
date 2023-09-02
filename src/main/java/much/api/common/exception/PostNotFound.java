package much.api.common.exception;

import static java.lang.String.format;
import static much.api.common.enums.Code.POST_NOT_FOUND;

public class PostNotFound extends MuchException {

    public PostNotFound(Long id) {
        super(POST_NOT_FOUND, format("게시글 [%s]를 찾을 수 없습니다.", id));
    }

    public PostNotFound(String category, Long id) {
        super(POST_NOT_FOUND, format("게시글 [%s: %s]를 찾을 수 없습니다.", category, id));
    }

}
