package much.api.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ImageResizeType {

    THUMBNAIL(480, 320),
    PROFILE(60, 60),
    NONE(0, 0);

    private final Integer width;

    private final Integer height;

}
