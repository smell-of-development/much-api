package much.api.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import much.api.dto.request.ProjectCreation;
import much.api.dto.request.ProjectModification;
import much.api.dto.request.ProjectSearch;
import much.api.dto.response.Envelope;
import much.api.dto.response.PagedResult;
import much.api.dto.response.ProjectDetail;
import much.api.dto.response.ProjectSummary;
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
                    - deadline (String)       : 모집 마감일 ex) 2023.11.15 (yyyy.MM.dd) - 필수
                    - startDate (String)      : 시작일 ex) 2023.09.10 (yyyy.MM.dd) / 시작일,종료일 모두 없어야 "협의"
                    - endDate (String)        : 종료일 ex) 2024.01.10 (yyyy.MM.dd) / 시작일,종료일 모두 없어야 "협의"
                    - timesPerWeek (String[]) : 모임주기 ex) ["월", "수", "금"] / 값이 없다면 "협의"
                    - <b>recruit (Object)     : 모집정보 - 필수</b>
                    - tags (String[])         : 스킬태그 ex) ["Java", "Spring", ...]
                    - introduction (String)   : 프로젝트 소개
                    ##### Object: recruit
                    - <b>positionStatus (Object[]) : 포지션 현황</b>
                    ##### Object: positionStatus
                    - name (String)        : 포지션 이름 - 필수
                    - needs (Number)       : 포지션 필요 인원 - 필수, 1 이상
                    - containsMe (Boolean) : 작성자 본인을 포함하는지 - 필수, 전체 포지션중 1개만 true
                    ### 응답값
                    - code 200
                    - 글 ID
                    - code 2000
                    - 로그인 된 사용자 미존재
                    """)
    ResponseEntity<Envelope<Long>> createProject(ProjectCreation request);


    @Operation(
            summary = "프로젝트 수정 API",
            description = """
                    프로젝트 글을 수정합니다.
                    - 로그인 사용자와 등록자가 같아야합니다.
                    - 요청예시 PUT /api/v1/projects/1
                    ### 요청값
                    - title (String)          : 프로젝트 제목 (40자 이하) - 필수
                    - imageUrl (String)       : 대표 이미지 url
                    - online (Boolean)        : 온라인 모임 여부 - 필수
                    - address (String)        : 모임 위치 주소 / 값이 없다면 "협의"
                    - deadline (String)       : 모집 마감일 ex) 2023.11.15 (yyyy.MM.dd) - 필수
                    - startDate (String)      : 시작일 ex) 2023.09.10 (yyyy.MM.dd) / 시작일,종료일 모두 없어야 "협의"
                    - endDate (String)        : 종료일 ex) 2024.01.10 (yyyy.MM.dd) / 시작일,종료일 모두 없어야 "협의"
                    - timesPerWeek (String[]) : 모임주기 ex) ["월", "수", "금"] / 값이 없다면 "협의"
                    - <b>recruit (Object)     : 모집정보 - 필수</b>
                    - tags (String[])         : 스킬태그 ex) ["Java", "Spring", ...]
                    - introduction (String)   : 프로젝트 소개
                    ##### Object: recruit
                    - <b>positionStatus (Object[]) : 포지션 현황 - 필수</b>
                    ##### Object: positionStatus
                    - id (Number)          : 포지션 고유 ID - 기존의 ID 또는 신규값 이라면 필요X
                    - name (String)        : 포지션 이름 - 필수
                    - needs (Number)       : 포지션 필요 인원 - 필수, 1 이상
                    - containsMe (Boolean) : 작성자 본인을 포함하는지 - 필수, 전체 포지션중 1개만 true
                    ### 응답값
                    - code 200
                    - 단건조회 내용
                    - code 2000
                    - 로그인 된 사용자를 찾을 수 없는경우
                    - 본인글이 아닌경우
                    """)
    //TODO 글 수정 응답값?
    ResponseEntity<Envelope<ProjectDetail>> modifyProject(Long projectId,
                                                          ProjectModification request);


    @Operation(summary = "프로젝트 상세 조회 API",
            description = """
                    id 에 해당하는 프로젝트의 상세정보를 조회합니다.
                    ### 요청
                    - (경로변수) id : 프로젝트 고유 ID
                    ### 응답값
                    - <b>writer     : 작성자 정보 (Object)</b>
                    - editable      : 수정가능 여부 (Boolean)
                    - id            : 고유 ID (Number)
                    - title         : 제목 (String)
                    - imageUrl      : 대표 이미지 url (String)
                    - online        : 온라인 모임여부 (Boolean)
                    - address       : 모임위치 (String) 협의라면 ""
                    - deadline      : 마감일 (String)
                    - deadlineDDay  : 마감일 D-Day (Number)
                    - startDate     : 일정 시작일 (String) 협의라면 ""
                    - endDate       : 일정 종료일 (String) 협의라면 ""
                    - between       : 시작일~종료일 사이 차이 (Number) 협의라면 ""
                    - timesPerWeek  : 모임주기 (String[]) 협의라면 ""
                    - <b>recruit    : 모집정보 (Object)</b>
                    - tags          : 태그 (String[])
                    - introduction  : 소개 (String)
                    ##### Object: writer
                    - id       : 작성자 고유 ID
                    - nickname : 작성자 닉네임
                    - imageUrl : 작성자 이미지
                    ##### Object: recruit
                    - state             : 전체 모집 상태 ({"code": String, "name": String})
                    - needs             : 전체 필요 인원 (Number)
                    - recruited         : 모집된 전체 인원 (Number)
                    - <b>positionStatus : 포지션 현황 (Object) - 필수</b>
                    ##### Object: positionStatus
                    - state      : 포지션 모집 상태 ({"code": String, "name": String})
                    - id         : 포지션 고유 ID (Number) 아래의 '포지션 이름' 이 같아도 고유 ID는 모두 다르다.
                    - name       : 포지션 이름 (String)
                    - needs      : 포지션 필요 인원 (Number)
                    - recruited  : 모집된 포지션 인원 (Number)
                    - containsMe : 작성자 == 조회자 == 해당 포지션 가입자
                    - deletable  : 작성자 == 조회자 && 수정시 해당 포지션 지울 수 있는가? (Boolean) 작성자 != 조회자 이면 false
                    """)
    ResponseEntity<Envelope<ProjectDetail>> getProject(Long projectId);


    ResponseEntity<Envelope<PagedResult<ProjectSummary>>> getProjects(ProjectSearch searchCondition);


    @Operation(
            summary = "프로젝트 삭제 API",
            description = """
                    프로젝트 글을 삭제합니다.
                    - 로그인 사용자와 등록자가 같아야합니다.
                    - 프로젝트와 연관된 신청, 가입정보 모두 삭제됩니다.
                    - 요청예시 DELETE /api/v1/projects/1
                    ### 응답값
                    - code 200
                    - 삭제 성공
                    - code 2000
                    - 로그인 된 사용자를 찾을 수 없는경우
                    - 본인글이 아닌경우
                    """)
    ResponseEntity<Envelope<Void>> deleteProject(Long projectId);
}
