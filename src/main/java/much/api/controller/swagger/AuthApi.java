package much.api.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import much.api.dto.response.Envelope;
import much.api.dto.Jwt;
import much.api.dto.response.OAuth2Response;
import much.api.dto.response.OAuth2UriResponse;
import org.springframework.http.ResponseEntity;

@Tag(name = "인증 API", description = "인증 관련 API")
public interface AuthApi {

    @Operation(summary = "OAuth2 URI 조회",
            description = """
                    카카오 혹은 구글 로그인 URI를 조회하는 API
                    ### 응답값 상세
                    - provider    : 요청 provider
                    - loginUri    : 요청한 provider 로그인 주소
                    """,
            parameters = {@Parameter(name = "provider", description = "kakao 또는 google")
            })
    ResponseEntity<Envelope<OAuth2UriResponse>> retrieveOAuth2Uri(String provider);

    @Operation(summary = "OAuth2 정보 처리",
            description = """
                    OAuth2 로그인 사용자 정보를 처리합니다.
                    로그인 시도시 자동으로 리다이렉트되어 요청됩니다.
                    ### 케이스별 결과값
                    1. 최초 사용자
                    - 휴대폰번호 중복(code: 8000)   : id(기존), provider(현재), socialId(현재), email(기존), phoneNumber(기존), loginUri => 통합 또는 기존로그인 유도
                    - 휴대폰번호 존재(code: 8100)   : id, provider(현재)  => 추가정보 등록 필요
                    - 휴대폰번호 미존재(code: 8101) : id, provider(현재)  => 추가정보 + 문자인증 필요
                    2. 기존 사용자
                    - 토큰발급(code: 200) : id, provider, email, accessToken, refreshToken
                    """,
            parameters = {
                    @Parameter(name = "provider", description = "kakao 또는 google"),
                    @Parameter(name = "code", description = "redirect uri 쿼리스트링에 포함된 code")
            })
    ResponseEntity<Envelope<OAuth2Response>> handleOAuth2(String provider, String code);


    @Operation(summary = "액세스토큰 갱신",
            description = """
                    액세스 토큰을 갱신합니다.
                    ### 요청값
                    - accessToken  : 만료되었거나 정상 액세스토큰
                    - refreshToken : 액세스 토큰에 해당하는 정상 리프레시토큰
                    ### 응답값
                    - 200  : accessToken - 리프레시 된 액세스 토큰
                    - 1000 : 요청값 이상
                    - 4100 : 비정상 토큰. 갱신불가
                    """,
            requestBody = @RequestBody(required = true))
    ResponseEntity<Envelope<Jwt>> refreshAccessToken(Jwt jwt);

}
