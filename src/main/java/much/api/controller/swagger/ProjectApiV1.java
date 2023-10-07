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
                    - writer        : 작성자 정보 (object)
                    - editable      : 수정가능 여부 (boolean)
                    - id            : 고유 ID (number)
                    - title         : 제목 (string)
                    - imageUrl      : 대표 이미지 url (string)
                    - isOnline      : 온라인 모임여부 (boolean)
                    - address       : 모임위치 (string)
                    - schedule      : 일정 (string)
                    - timesPerWeek  : 모임주기 (string)
                    - deadline      : 마감일 (string)
                    - deadlineDDay  : 마감일 D-Day (number)
                    - recruit       : 모집정보 (object)
                    - tags          : 태그 (string[])
                    - introduction  : 소개 (string)
                    """)
    ResponseEntity<Envelope<ProjectDetail>> getProject(Long id);
}
