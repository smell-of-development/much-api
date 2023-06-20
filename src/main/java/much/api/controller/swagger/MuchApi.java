package much.api.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import much.api.dto.request.MuchRegistration;
import much.api.dto.response.Envelope;
import much.api.dto.response.MuchDetail;
import org.springframework.http.ResponseEntity;

@Tag(name = "프로젝트 및 스터디 관련 API")
public interface MuchApi {

    @Operation(summary = "프로젝트 등록 API",
            description = """
                    프로젝트를 등록합니다.
                    로그인이 되어있는 가입된 유저만 등록할 수 있습니다.
                    - 일자값은 ISO 8601 형식. ex.2023-06-19T09:00:00
                    - 자바스크립트 ISO 8601 형식 반환 : Date.toISOString()
                    ### 요청값
                    - title        : 제목(string)
                    - imageUrl     : 대표 이미지 url(string)
                    - online       : 온라인 모임여부(boolean)
                    - location     : 모임위치(string)
                    - deadline     : 모집 마감일(string)
                    - startDate    : 시작일(string)
                    - endDate      : 종료일(string)
                    - schedule     : 모임주기(string) ex. 월목금토
                    - target       : 모임대상(string)
                    - introduction : 소개(string)
                    - skills       : 스킬태그(string[]) ex. ["Java", "Spring", ...]
                    - work         : 포지션별 모집인원([{"position": "string", "needs": number}, ...])
                    ### 응답값
                    - code 200  : 등록 성공, 고유 ID 반환 ex. {..., "result": 1}
                    - code 1100 : 요청필드 누락 등 잘못된 형식
                    - code 2000 : 등록요청 사용자 미존재
                    """)
    ResponseEntity<Envelope<Long>> registerProject(MuchRegistration request);

    @Operation(summary = "프로젝트 상세 조회 API",
            description = """
                    id 에 해당하는 프로젝트의 상세정보를 조회합니다.
                    ### 요청
                    - 경로변수 id : 프로젝트 고유 ID
                    ### 응답값
                    - id            : 고유 ID
                    - title         : 제목(string)
                    - writer        : 작성자({"id": number(고유 ID), "nickname": "string", "pictureUrl": "string"})
                    - imageUrl      : 대표 이미지 url(string)
                    - online        : 온라인 모임여부(boolean)
                    - location      : 모임위치(string)
                    - deadline      : 모집 마감일(string)
                    - startDate     : 시작일(string)
                    - endDate       : 종료일(string)
                    - schedule      : 모임주기(string) ex. 월, 목, 금, 토
                    - target        : 모임대상(string)
                    - currentTotal  : 현재 전체 포지션 모집인원 합(number)
                    - maximumPeople : 포지션별 모집인원 전체 총합(number)
                    - introduction  : 소개(string)
                    - skills        : 스킬([{"name": "string", "imageUrl": "string"}]])
                    - work          : 포지션별 현재인원/모집인원([{"position": "string", "current": number, "needs": number}, ...])
                    """)
    ResponseEntity<Envelope<MuchDetail>> retrieveProject(Long id);

}
