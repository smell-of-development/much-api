package much.api.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import much.api.dto.response.Envelope;
import much.api.dto.Jwt;
import much.api.dto.response.OAuth2;
import much.api.dto.response.OAuth2Uri;
import much.api.dto.response.SmsCertification;
import org.springframework.http.ResponseEntity;

@Tag(name = "인증 API", description = "인증 관련 API")
public interface AuthApi {

    @Operation(summary = "OAuth2 URI 조회",
            description = """
                    카카오 혹은 구글 로그인 URI를 조회하는 API
                    ### 응답값 상세
                    - provider : 요청 provider
                    - loginUri : 요청한 provider 로그인 주소
                    """,
            parameters = {@Parameter(name = "provider", description = "kakao 또는 google")
            })
    ResponseEntity<Envelope<OAuth2Uri>> retrieveOAuth2Uri(String provider);

    @Operation(summary = "OAuth2 정보 처리",
            description = """
                    OAuth2 로그인 사용자 정보를 처리합니다.
                    ### 케이스별 결과값
                    1. 최초 사용자
                    - 휴대폰번호 존재(code: 8100)   : id => 추가정보 등록 필요
                    - 휴대폰번호 미존재(code: 8101) : id => 추가정보 + 문자인증 필요
                    2. 기존 사용자 / 기존 사용자와 휴대폰번호가 같은 최초 사용자
                    - 토큰발급(code: 200) : id, accessToken, refreshToken
                    """,
            parameters = {
                    @Parameter(name = "provider", description = "kakao 또는 google"),
                    @Parameter(name = "code", description = "redirect uri 쿼리스트링에 포함된 code")
            })
    ResponseEntity<Envelope<OAuth2>> handleOAuth2(String provider, String code);


    @Operation(summary = "액세스토큰 갱신",
            description = """
                    액세스 토큰을 갱신합니다.
                    ### 요청값
                    - accessToken  : 만료되었거나 정상인 액세스토큰
                    - refreshToken : 액세스 토큰에 해당하는 정상 리프레시토큰
                    ### 응답값
                    - code 200  : accessToken - 리프레시 된 액세스 토큰
                    - code 1000 : 요청값 이상 (accessToken, refreshToken 모두 필수)
                    - code 4000 : 갱신불가
                    """,
            requestBody = @RequestBody(required = true))
    ResponseEntity<Envelope<Jwt>> refreshAccessToken(Jwt jwt);


    @Operation(summary = "로그인 체크",
            description = """
                    액세스 토큰을 검사하여 로그인 여부를 체크합니다.
                    Authorization 헤더에 액세스 토큰이 필요합니다.
                    ### 요청값
                    - (Header) Authorization : Bearer {accessToken}
                    ### 응답값
                    - code 200  : 정상토큰 ex) result: 1 (사용자의 id)
                    - code 4000 : 토큰이 없거나 비정상
                    """)
    ResponseEntity<Envelope<Long>> checkToken();


    @Operation(summary = "SMS 인증번호 발송",
            description = """
                    phoneNumber 쿼리스트링의 휴대폰번호로 인증번호를 발송합니다.
                    010####@@@@ 형식이어야만 합니다.
                    ### 응답값 상세
                    - code 200  : phoneNumber - 휴대폰번호, remainTimeInSeconds - 남은시간(초)
                    - code 1000 : phoneNumber 파라미터가 없음
                    - code 8001 : 휴대폰번호 형식이 아님
                    """,
            parameters = {@Parameter(name = "phoneNumber", description = "휴대폰번호")
            })
    ResponseEntity<Envelope<SmsCertification>> sendCertificationNumber(String phoneNumber);

}
