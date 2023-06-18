package much.api.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import much.api.dto.request.MuchRegistration;
import much.api.dto.response.Envelope;
import org.springframework.http.ResponseEntity;

@Tag(name = "프로젝트 및 스터디 등록 API")
public interface MuchApi {

    @Operation(summary = "프로젝트 등록 API",
            description = """
                    프로젝트를 등록합니다.
                    - 일자값은 ISO 8601 형식. ex.2023-06-19T09:00:00
                    - 자바스크립트 ISO 8601 형식 반환 : Date.toISOString()
                    ### 요청값
                    - title        : 제목(string)
                    - imageUrl     : 대표 이미지 url(string)
                    - isOnline     : 온라인 모임여부(boolean)
                    - location     : 모임위치(string)
                    - deadline     : 모집 마감일(string)
                    - startDate    : 시작일(string)
                    - endDate      : 종료일(string)
                    - schedule     : 모임주기(string) ex. 월목금토
                    - target       : 모임대상(string)
                    - introduction : 소개(string)
                    - skills       : 스킬태그(string[]) ex. ["Java", "Spring", ...]
                    - work         : 포지션별 인원([{"position": "string", "needs": number}, ...])
                    ### 응답값
                    - code 200  : 등록 성공
                    - code 1100 : 요청값 이상
                    """)
    ResponseEntity<Envelope<Void>> registerProject(MuchRegistration request);

}
