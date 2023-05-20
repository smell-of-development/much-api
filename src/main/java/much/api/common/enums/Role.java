package much.api.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    ROLE_USER("유저"), ROLE_ADMIN("관리자");

    private final String description;

}
