package much.api.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import much.api.dto.Jwt;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OAuth2Response extends Jwt {

    private final Long id;

    private final String socialId;

    @Schema(description = "현재 인증을 진행한 provider", example = "kakao")
    private final String provider;

    private final String email;

    @Schema(description = "마스킹 된 휴대폰번호", example = "010-****-5678")
    private final String phoneNumber;

    @Schema(description = "휴대폰번호 중복일 때, 기존 로그인을 위한 주소")
    private final String loginUri;


    @Builder
    private OAuth2Response(String accessToken,
                           String refreshToken,

                           Long id,
                           String socialId,
                           String provider,
                           String email,
                           String phoneNumber,
                           String loginUri) {

        super(accessToken, refreshToken);
        this.id = id;
        this.socialId = socialId;
        this.provider = provider;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.loginUri = loginUri;
    }

}