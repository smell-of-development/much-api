package much.api.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@RequiredArgsConstructor
public enum OAuth2Provider {
    KAKAO("kakao"), GOOGLE("google");

    private final String name;

}
