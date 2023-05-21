package much.api.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import much.api.common.enums.Code;
import much.api.dto.response.Envelope;
import much.api.dto.response.PositionResponse;
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
    ResponseEntity<Envelope<PositionResponse>> retrievePositions();

    @Operation(summary = "코드 목록 조회",
            description = """
                    서버에서 응답하는 모든 코드와 메세지들을 조회합니다.
                    ### 응답값 설명
                    - result[] : {"id": integer, "message": "string"}
                    """)
    ResponseEntity<Envelope<Code[]>> retrieveCodes();

}
