package much.api.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MuchState {

    DONE("모집완료"),
    RECRUITING("모집중");

    private final String meaning;

}
