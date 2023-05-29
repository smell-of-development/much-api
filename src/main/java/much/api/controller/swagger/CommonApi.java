package much.api.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import much.api.common.enums.Code;
import much.api.dto.response.Envelope;
import much.api.dto.response.Positions;
import org.springframework.http.ResponseEntity;


@Tag(name = "공통 API")
public interface CommonApi {

    @Operation(summary = "포지션 목록 조회",
            description = """
                    대분류와 중분류 포지션 목록을 조회한다.
                    ### 응답값 설명
                    - result.positions[]            : 대분류
                    - result.positions[].children[] : 중분류
                    """)
    ResponseEntity<Envelope<Positions>> retrievePositions();

    @Operation(summary = "코드 목록 조회",
            description = """
                    서버에서 응답하는 모든 코드와 메세지들을 조회합니다.
                    ### 응답값 설명
                    - result[] : {"id": integer, "message": "string"}
                    """)
    ResponseEntity<Envelope<Code[]>> retrieveCodes();


    @Operation(summary = "중복닉네임 조회",
            description = """
                    닉네임 유효성 검사와 중복 닉네임 검사 API
                    ### 응답값 설명
                    - code 200  : 사용할 수 있는 닉네임
                    - code 1000 : nickname 파라미터가 없음
                    - code 1001 : 미입력
                    - code 2002 : 닉네임은 2글자 이상 완성된 한글, 영어, 숫자만 사용할 수 있습니다.
                    - code 2003 : 중복되는 닉네임이 있어 사용할 수 없습니다.
                    """)
    ResponseEntity<Envelope<Void>> retrieveDuplicatedNickname(String nickname);

}
