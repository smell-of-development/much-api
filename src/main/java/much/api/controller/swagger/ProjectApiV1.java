package much.api.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import much.api.dto.request.ProjectCreation;
import much.api.dto.response.Envelope;
import much.api.dto.response.ProjectDetail;
import org.springframework.http.ResponseEntity;

@Tag(name = "프로젝트 관련 API")
public interface ProjectApiV1 {

    @Operation(summary = "프로젝트 등록 API",
            description = """
                    프로젝트를 등록합니다.
                    - 로그인이 되어있어야 합니다.
                    ### 요청값
                    - title (String)          : 프로젝트 제목 (40자 이하) - 필수
                    - imageUrl (String)       : 대표 이미지 url
                    - online (Boolean)        : 온라인 모임 여부 - 필수
                    - address (String)        : 모임 위치 주소 / 값이 없다면 "협의"
                    - deadline (String)       : 모집 마감일 ex) 2023-11-15 (yyyy-MM-dd) - 필수
                    - startDate (String)      : 시작일 ex) 2023-09-10 (yyyy-MM-dd) / 시작일,종료일 모두 없어야 "협의"
                    - endDate (String)        : 종료일 ex) 2024-01-10 (yyyy-MM-dd) / 시작일,종료일 모두 없어야 "협의"
                    - timesPerWeek (String[]) : 모임주기 ex) ["월", "수", "금"] / 값이 없다면 "협의"
                    - recruit (Object[])      : [{name: 포지션 이름(String), needs: 필요 인원(Number)}, ...] - 필수
                    - tags (String[])         : 스킬태그 ex) ["Java", "Spring", ...]
                    - introduction (String)   : 프로젝트 소개
                    ### 응답값
                    - code 200
                    - 글 ID
                    - code 2000
                    - 로그인 된 사용자 미존재
                    """)
    ResponseEntity<Envelope<Long>> createProject(ProjectCreation request);

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
    ResponseEntity<Envelope<ProjectDetail>> getProject(Long id);

}
