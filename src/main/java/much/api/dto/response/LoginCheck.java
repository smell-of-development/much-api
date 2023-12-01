package much.api.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginCheck {

    private Long id;

    private String nickname;

    private String imageUrl;

}
