package much.api.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import much.api.dto.Jwt;
import much.api.dto.request.JoinInformation;
import much.api.dto.request.SocialUserLinking;
import much.api.dto.response.Envelope;
import org.springframework.http.ResponseEntity;

@Tag(name = "유저 API", description = "유저 관련 API")
public interface UserApi {


    @Operation(summary = "사용자등록 및 로그인처리",
            description = """
                    유저를 등록하고, 로그인 토큰을 발급받습니다.
                    ### 요청값(모두 필수)
                    - id             : 사용자의 로그인 id
                    - password       : 로그인 패스워드
                    - nickname       : 닉네임
                    - phoneNumber    : 휴대폰번호
                    - positionParent : 포지션 대분류
                    - positionChild  : 포지션 중분류
                    - positionClass  : 포지션 수준
                    ### 응답값
                    - code 200  : 설정 성공, 토큰 발급 (id, accessToken, refreshToken)
                    - code 1000 : 필수값 미존재 및 불일치
                    
                    - code 2007 : ID는 4글자 이상 20글자 이하 영어, 숫자만 사용할 수 있습니다.
                    - code 2006 : 중복되는 로그인 ID가 있어 사용할 수 없습니다.
                    
                    - code 2002 : 닉네임은 2글자 이상 8글자 이하 완성된 한글, 영어, 숫자만 사용할 수 있습니다.
                    - code 2003 : 중복되는 닉네임이 있어 사용할 수 없습니다.
                    
                    - code 2008 : 비밀번호는 공백을 제외한 8글자 이상 20글자 이하만 가능합니다.
                    
                    - code 8000 : 휴대폰 번호 중복
                    - code 8001 : 휴대폰번호 형식이 아님
                    """,
            requestBody = @RequestBody(required = true, description = "사용자 id, 휴대폰번호, 포지션 대분류, 포지션 중분류, 포지션수준"))
    ResponseEntity<Envelope<Jwt>> registerUser(JoinInformation request);

    @Operation(summary = "사용자 소셜 연동처리",
            description = """
                    요청 phoneNumber 에 해당하는 사용자를 targetId 에 해당하는 사용자와 연동합니다.
                                        
                    소셜로그인 후 받은 id 값을 toBeDeletedId 로 설정하고(연동시 현재 로그인 시도한 정보를 지워야 합니다.),
                    인증번호 확인시 중복 응답을 받은 휴대폰번호를 targetPhoneNumber 으로 설정해야 합니다(해당 번호의 사용자와 연동됩니다.).
                    ### 요청값
                    - toBeDeletedId     : 현재 로그인을 시도한 사용자 id
                    - targetPhoneNumber : 연동이 될 사용자의 휴대폰번호
                    ### 응답값
                    - code 200  : 설정 성공, 토큰 발급 (id, accessToken, refreshToken)
                    - code 1000 : 필수값 미존재
                    - code 2000 : 사용자를 찾을 수 없음 (현재 로그인 시도 id)
                    - code 2004 : 전화번호에 해당하는 사용자를 찾을 수 없음
                    - code 8001 : 휴대폰번호 형태가 아님
                    """,
            requestBody = @RequestBody(required = true, description = "현재 id, 연동 대상 휴대폰 번호"))
    ResponseEntity<Envelope<Jwt>> linkSocialUser(SocialUserLinking request);

}
