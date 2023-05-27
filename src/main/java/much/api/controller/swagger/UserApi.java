package much.api.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import much.api.dto.request.PositionSetting;
import much.api.dto.response.Envelope;
import much.api.dto.response.OAuth2;
import org.springframework.http.ResponseEntity;

@Tag(name = "유저 API", description = "유저 관련 API")
public interface UserApi {


    @Operation(summary = "유저 포지션 설정 및 로그인처리",
            description = """
                    id에 해당하는 유저의 포지션을 설정하고 로그인 토큰을 발급받습니다.
                    ### 요청값
                    - id            : 사용자 id
                    - positionIds   : 포지션 id를 ',' 로 이어붙인 문자열 ex) 100,101
                    - positionClass : 포지션 수준 ex) 하수
                    ### 응답값
                    - code 200  : 설정 성공, 토큰 발급 (id, accessToken, refreshToken)
                    - code 1000 : 요청값 이상
                    - code 2000 : 사용자를 찾을 수 없음
                    - code 8102 : 전화번호 인증을 받지 않음
                    """,
            requestBody = @RequestBody(required = true, description = "사용자 id, 포지션ids, 포지션수준"))
    ResponseEntity<Envelope<OAuth2>> initUser(PositionSetting request);
}
