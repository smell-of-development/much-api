package much.api.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import much.api.dto.response.WebToken;
import much.api.dto.request.Login;
import much.api.dto.request.SmsVerification;
import much.api.dto.response.Envelope;
import much.api.dto.response.SmsCertification;
import org.springframework.http.ResponseEntity;

@Tag(name = "인증 API", description = "인증 관련 API")
public interface AuthApi {

    @Operation(summary = "Swagger 테스트용 토큰 발급받기",
            description = """
                    개발모드일때만 동작합니다. id에 해당하는 토큰을 받습니다.
                    ### 요청값
                    - id : long
                    - ex) /testToken?id=1
                    ### 응답값
                    - id, accessToken, refreshToken
                    """)
    ResponseEntity<Envelope<WebToken>> testToken(long id);

    @Operation(summary = "로그인",
            description = """
                    로그인을 요청합니다.
                    ### 요청값
                    - id       : id
                    - password : 비밀번호
                    ### 응답값
                    - id, accessToken, refreshToken
                    """,
            requestBody = @RequestBody(required = true))
    ResponseEntity<Envelope<WebToken>> login(Login request);

//    @Operation(summary = "OAuth2 URI 조회",
//            description = """
//                    카카오 혹은 구글 로그인 URI를 조회하는 API
//                    ### 응답값
//                    - provider : 요청 provider
//                    - loginUri : 요청한 provider 로그인 주소
//                    """,
//            parameters = {@Parameter(name = "provider", description = "kakao 또는 google")
//            })
//    ResponseEntity<Envelope<OAuth2Uri>> retrieveOAuth2Uri(String provider);

//    @Operation(summary = "OAuth2 정보 처리",
//            description = """
//                    OAuth2 로그인 사용자 정보를 처리합니다.
//                    ### 케이스별 결과값
//                    1. 최초 사용자
//                    - 휴대폰번호 존재(code: 8100)   : id => 추가정보 등록 필요
//                    - 휴대폰번호 미존재(code: 8101) : id => 추가정보 + 문자인증 필요.
//                    2. 기존 사용자 / 기존 사용자와 휴대폰번호가 같은 최초 사용자
//                    - 토큰발급(code: 200) : id, accessToken, refreshToken
//                    """,
//            parameters = {
//                    @Parameter(name = "provider", description = "kakao 또는 google"),
//                    @Parameter(name = "code", description = "redirect uri 쿼리스트링에 포함된 code")
//            })
//    ResponseEntity<Envelope<Jwt>> handleOAuth2(String provider, String code);


    @Operation(summary = "액세스토큰 갱신",
            description = """
                    액세스 토큰을 갱신합니다.
                    ### 요청값
                    - accessToken  : 만료되었거나 정상인 액세스토큰(필수)
                    - refreshToken : 액세스 토큰에 해당하는 정상 리프레시토큰(필수)
                    ### 응답값
                    - result{}: accessToken - 리프레시 된 액세스 토큰
                    """,
            requestBody = @RequestBody(required = true))
    ResponseEntity<Envelope<WebToken>> refreshAccessToken(WebToken request);


    @Operation(summary = "로그인 체크",
            description = """
                    액세스 토큰을 검사하여 로그인 여부를 체크합니다.
                    Authorization 헤더에 액세스 토큰이 필요합니다.
                    ### 요청값
                    - (Header) Authorization : Bearer {accessToken}
                    ### 응답값
                    - code 200  : 정상토큰 => ex) result(number): 1 (사용자의 id)
                    - code 4001 : 토큰이 없거나 비정상
                    """)
    ResponseEntity<Envelope<Long>> checkToken();


    @Operation(summary = "가입 SMS 인증번호 발송",
            description = """
                    가입을 위해 휴대폰번호로 인증번호를 발송합니다.
                    - 개발환경 + 프로퍼티 smsPass: true 설정시 인증번호를 보내지 않고, 성공을 응답합니다.
                    ### 요청값
                    - phoneNumber : 010####@@@@ 형식이어야 합니다.
                    ### 응답값
                    - code 200  : phoneNumber - 휴대폰번호, remainTimeInMinutes - 남은시간(분)
                    - code 2000
                    - 휴대폰 번호 중복
                    - 휴대폰번호 형식이 아님
                    - 메세지 발송 실패
                    - 하루 최대 인증번호 전송횟수를 초과
                    """,
            parameters = {
                    @Parameter(name = "phoneNumber", description = "휴대폰번호")
            })
    ResponseEntity<Envelope<SmsCertification>> sendJoinCertificationNumber(String phoneNumber);


    @Operation(summary = "가입 SMS 인증번호 확인",
            description = """
                    발송된 인증번호를 확인하고, 휴대폰번호를 설정합니다.
                    - 개발환경 + 프로퍼티 smsPass: true 설정시 모든 인증번호에 대해 성공을 응답합니다.
                    ### 요청값
                    - id                  : 사용자 id
                    - phoneNumber         : 인증번호를 받은 휴대폰번호. 010####@@@@ 형식
                    - certificationNumber : 전송받은 인증번호
                    ### 응답값
                    - code 200  : 인증성공
                    - code 2000
                    - 휴대폰번호 형식이 아님
                    - 인증번호가 일치하지 않음
                    - 발송기록을 찾을 수 없음
                    """,
            requestBody = @RequestBody(required = true, description = "사용자 id, 휴대폰번호, 인증번호"))
    ResponseEntity<Envelope<Void>> verifyJoinCertificationNumber(SmsVerification request);

}
