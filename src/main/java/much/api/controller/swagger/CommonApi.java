package much.api.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import much.api.common.enums.ResponseCode;
import much.api.dto.response.Envelope;
import much.api.dto.response.Positions;
import org.springframework.http.ResponseEntity;


@Tag(name = "공통 API")
public interface CommonApi {

    @Operation(summary = "포지션 목록 조회",
            description = """
                    대분류와 중분류 포지션 목록을 조회한다.
                    ### 응답값 설명
                    - result.positions.jobGroups : 직군
                    - result.positions.careers   : 경력
                    """)
    ResponseEntity<Envelope<Positions>> retrievePositions();

    @Operation(summary = "코드 목록 조회",
            description = """
                    서버에서 응답하는 모든 코드와 메세지들을 조회합니다.
                    ### 응답값 설명
                    - result[] : {"id": integer, "message": "string"}
                    """)
    ResponseEntity<Envelope<ResponseCode[]>> retrieveCodes();

    @Operation(summary = "중복 로그인 ID 조회",
            description = """
                    로그인 ID 유효성 검사와 중복 닉네임 검사 API
                    ### 응답값 설명
                    - responseCode 200  : 사용할 수 있는 로그인 ID
                    - responseCode 1000 : 값 미존재
                    - responseCode 2007 : ID는 4글자 이상 20글자 이하 영어, 숫자만 사용할 수 있습니다.
                    - responseCode 2006 : 중복되는 로그인 ID가 있어 사용할 수 없습니다.
                    """)
    ResponseEntity<Envelope<Void>> retrieveDuplicatedLoginId(String id);

    @Operation(summary = "중복 닉네임 조회",
            description = """
                    닉네임 유효성 검사와 중복 닉네임 검사 API
                    ### 응답값 설명
                    - responseCode 200  : 사용할 수 있는 닉네임
                    - responseCode 1000 : 값 미존재
                    - responseCode 2002 : 닉네임은 2글자 이상 8글자 이하 완성된 한글, 영어, 숫자만 사용할 수 있습니다.
                    - responseCode 2003 : 중복되는 닉네임이 있어 사용할 수 없습니다.
                    """)
    ResponseEntity<Envelope<Void>> retrieveDuplicatedNickname(String nickname);

}
