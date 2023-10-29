package much.api.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import much.api.dto.request.ProjectApplicationForm;
import much.api.dto.request.ProjectForm;
import much.api.dto.request.ProjectSearch;
import much.api.dto.response.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "프로젝트 관련 API")
public interface ProjectApiV1 {

    @Operation(
            summary = "프로젝트 등록 API",
            description = """
                    프로젝트를 등록합니다.
                    - 로그인이 되어있어야 합니다.
                    ### 요청값
                    - title (String)         : 프로젝트 제목 (40자 이하) - 필수
                    - imageUrl (String)      : 대표 이미지 url
                    - online (Boolean)       : 온라인 모임 여부 - 필수
                    - address (String)       : 모임 위치 주소 / 값이 없다면 "협의"
                    - deadline (String)      : 모집 마감일 ex) 2023.11.15 (yyyy.MM.dd) - 필수
                    - startDate (String)     : 시작일 ex) 2023.09.10 (yyyy.MM.dd) / 시작일,종료일 모두 없어야 "협의"
                    - endDate (String)       : 종료일 ex) 2024.01.10 (yyyy.MM.dd) / 시작일,종료일 모두 없어야 "협의"
                    - meetingDays (String[]) : 모임일 ex) ["월", "수", "금"] / 값이 없다면 "협의"
                    - <b>recruit (Object)    : 모집정보 - 필수</b>
                    - tags (String[])        : 스킬태그 ex) ["Java", "Spring", ...]
                    - introduction (String)  : 프로젝트 소개
                    ##### Object: recruit
                    - <b>positionStatus (Object[]) : 포지션 현황</b>
                    ##### Object: positionStatus
                    - name (String)        : 포지션 이름 - 필수
                    - needs (Number)       : 포지션 필요 인원 - 필수, 1 이상
                    - containsMe (Boolean) : 작성자 본인을 포함하는지 - 필수, 전체 포지션중 1개만 true
                    ### 응답값
                    - code 200
                    - id (number) : 프로젝트 글 ID
                    - code 2000
                    - 로그인 된 사용자 미존재
                    """)
    ResponseEntity<Envelope<Long>> createProject(ProjectForm request);


    @Operation(
            summary = "프로젝트 수정 API",
            description = """
                    프로젝트 글을 수정합니다.
                    - 로그인 사용자와 등록자가 같아야합니다.
                    - 요청예시 PUT /v1/projects/1
                    ### 요청값
                    - title (String)         : 프로젝트 제목 (40자 이하) - 필수
                    - imageUrl (String)      : 대표 이미지 url
                    - online (Boolean)       : 온라인 모임 여부 - 필수
                    - address (String)       : 모임 위치 주소 / 값이 없다면 "협의"
                    - deadline (String)      : 모집 마감일 ex) 2023.11.15 (yyyy.MM.dd) - 필수
                    - startDate (String)     : 시작일 ex) 2023.09.10 (yyyy.MM.dd) / 시작일,종료일 모두 없어야 "협의"
                    - endDate (String)       : 종료일 ex) 2024.01.10 (yyyy.MM.dd) / 시작일,종료일 모두 없어야 "협의"
                    - meetingDays (String[]) : 모임일 ex) ["월", "수", "금"] / 값이 없다면 "협의"
                    - <b>recruit (Object)    : 모집정보 - 필수</b>
                    - tags (String[])        : 스킬태그 ex) ["Java", "Spring", ...]
                    - introduction (String)  : 프로젝트 소개
                    ##### Object: recruit
                    - <b>positionStatus (Object[]) : 포지션 현황 - 필수</b>
                    ##### Object: positionStatus
                    - id (Number)          : 포지션 고유 ID - 기존의 포지션 ID 또는 신규 포지션 이라면 필요 없음
                    - name (String)        : 포지션 이름 (20자 이하) - 필수
                    - needs (Number)       : 포지션 필요 인원 - 필수, 1 이상
                    - containsMe (Boolean) : 작성자 본인을 포함하는지 - 필수, 전체 포지션중 1개만 true
                    ### 응답값
                    - code 200
                    - id (number) : 프로젝트 글 ID
                    - code 2000
                    - 로그인 된 사용자를 찾을 수 없는경우
                    - 본인글이 아닌경우
                    """)
    ResponseEntity<Envelope<Long>> modifyProject(Long projectId,
                                                 ProjectForm request);


    @Operation(
            summary = "프로젝트 상세 조회 API",
            description = """
                    id 에 해당하는 프로젝트의 상세정보를 조회합니다.
                    ### 요청
                    - (경로변수) id : 프로젝트 고유 ID
                    ### 응답값
                    - <b>writer      : 작성자 정보 (Object)</b>
                    - editable       : 수정가능 여부. 즉, 작성자인지 (Boolean)
                    - alreadyJoined  : 현재 로그인 사용자가 가입되었는지 (Boolean) 비로그인은 ""
                    - alreadyApplied : 현재 로그인 사용자가 신청했는지 (Boolean) 비로그인은 ""
                    - id             : 고유 ID (Number)
                    - title          : 제목 (String)
                    - imageUrl       : 대표 이미지 url (String)
                    - online         : 온라인 모임여부 (Boolean)
                    - address        : 모임위치 (String) 협의라면 ""
                    - deadline       : 마감일 (String)
                    - deadlineDDay   : 마감일 D-Day (Number)
                    - startDate      : 일정 시작일 (String) 협의라면 ""
                    - endDate        : 일정 종료일 (String) 협의라면 ""
                    - between        : 시작일~종료일 사이 차이 (Number) 협의라면 ""
                    - meetingDays    : 모임일 (String[]) 협의라면 ""
                    - <b>recruit     : 모집정보 (Object)</b>
                    - tags           : 태그 (String[])
                    - introduction   : 소개 (String)
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


    @Operation(
            summary = "프로젝트 다건조회 API",
            description = """
                    # 검색기능에서 태그검색만 구현되어 있고, 검색어(search) 값 제목검색은 구현되어있지 않습니다.  
                    프로젝트를 다건 조회합니다.
                    - 페이지 결과를 얻습니다.
                    - 요청예시 GET /v1/projects?search=제목&page=1&size=16&tags=Spring,JPA
                    ### 정확도순 정렬시(byRecent=false) 우선순위
                    - 태그 일치 개수 순서이므로 요청값 'tags' 존재해야 의미있음.
                    ### 요청값
                    - 쿼리스트링(search)         - String  : (선택) 제목 또는 내용 검색어
                    - 쿼리스트링(tags)           - String  : (선택) 검색할 태그들을 "," 으로 이어붙인 문자열
                    - 쿼리스트링(page)           - String  : (선택) 검색할 페이지. 기본값 1
                    - 쿼리스트링(size)           - String  : (선택) 검색할 페이지당 사이즈. 기본값 16, 최대 40
                    - 쿼리스트링(onlyRecruiting) - String  : (선택) true - 모집중만 보기, false - 전체보기. 기본값 false
                    - 쿼리스트링(byRecent)       - Boolean : (선택) true - 최신순 정렬, false - 정확도순 정렬. 기본값 true
                    ### 응답값
                    - code 200 : 정상조회
                    - result.elements[]       : 페이징 된 결과들
                    - result.page             : 현재 페이지
                    - result.numberOfElements : 현재 페이지 요소들의 수
                    - result.totalPages       : 전체 페이지
                    - result.first            : 첫번째 페이지인지 여부 true / false
                    - result.last             : 마지막 페이지인지 여부 true / false
                    ##### elements[]
                    - id           : 고유 ID (Number)
                    - title        : 제목 (String)
                    - tags[]       : 태그 (String[])
                    - online       : 온라인 모임여부 (Boolean)
                    - address      : 모임위치 (String) 협의라면 ""
                    - timesPerWeek : 주 모임주기 (Number)
                    - viewCount    : 조회수 (Number)
                    - deadlineDDay : 마감일 D-Day (Number)
                    - pick         : 찜 여부 (Boolean)
                    - imageUrl     : 대표 이미지 url (String)
                    - <b>recruit   : 모집정보 (Object)</b>
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
                    """)
    ResponseEntity<Envelope<PagedResult<ProjectSummary>>> getProjects(ProjectSearch searchCondition);


    @Operation(
            summary = "프로젝트 삭제 API",
            description = """
                    프로젝트 글을 삭제합니다.
                    - 로그인 사용자와 등록자가 같아야합니다.
                    - 프로젝트와 연관된 신청, 가입정보 모두 삭제됩니다.
                    - 요청예시 DELETE /v1/projects/1
                    ### 응답값
                    - code 200
                    - 삭제 성공
                    - code 2000
                    - 로그인 된 사용자를 찾을 수 없는경우
                    - 본인글이 아닌경우
                    """)
    ResponseEntity<Envelope<Void>> deleteProject(Long projectId);


    @Operation(
            summary = "프로젝트 신청 API",
            description = """
                    프로젝트의 특정 포지션을 신청합니다.
                    - 로그인이 되어있어야 합니다.
                    ### 요청값
                    - (경로변수) projectId : 프로젝트 고유 ID
                    - positionId (Number)  : 프로젝트 포지션 ID (프로젝트 상세조회의 positionStatus.id) - 필수
                    - memo (String)        : 신청자의 메모
                    ### 응답값
                    - code 200
                    - 신청 완료
                    - code 2000
                    - 로그인 된 사용자를 찾을 수 없는경우
                    - 프로젝트를 찾을 수 없는경우
                    - 이미 가입된 프로젝트인 경우
                    - 이미 신청한 프로젝트인 경우
                    - 프로젝트의 포지션을 찾을 수 없는경우
                    """)
    ResponseEntity<Envelope<Void>> createProjectApplication(Long projectId, ProjectApplicationForm request);


    @Operation(
            summary = "프로젝트 신청서 삭제(취소) API",
            description = """
                    신청한 프로젝트 신청서를 삭제합니다.
                    - 로그인이 되어있어야 합니다.
                    ### 요청값
                    - (경로변수) projectId : 신청서를 삭제할 프로젝트 고유 ID
                    ### 응답값
                    - code 200
                    - 삭제 완료
                    - code 2000
                    - 로그인 된 사용자를 찾을 수 없는경우
                    - 프로젝트에 해당하는 신청서를 찾을 수 없는경우
                    - 본인의 신청서가 아닌경우
                    """)
    ResponseEntity<Envelope<Void>> deleteProjectApplication(Long projectId);


    @Operation(
            summary = "프로젝트 신청서 목록 얻어오기 API",
            description = """
                    프로젝트에 신청한 신청서들을 요청합니다.
                    ### 요청값
                    - (경로변수) projectId : 프로젝트 고유 ID - 요청자가 생성한 프로젝트
                    ### 응답값
                    - code 200
                    - result[].applicant    : 지원자 정보 (Object)
                    - result[].id           : 신청서 고유 ID (Number)
                    - result[].positionName : 신청 포지션 이름 (String)
                    - result[].memo         : 신청시의 메모 (String)
                    ##### Object: applicant
                    - id       : 지원자 고유 ID
                    - nickname : 지원자 닉네임
                    - imageUrl : 지원자 이미지
                    - code 2000
                    - 프로젝트를 찾을 수 없는경우
                    - 프로젝트 생성자가 아닌경우
                    """)
    ResponseEntity<Envelope<List<ProjectApplication>>> getProjectApplications(Long projectId);


    @Operation(
            summary = "프로젝트 신청 승인하기",
            description = """
                    프로젝트 신청서를 승인합니다.
                    ### 요청값
                    - (경로변수) applicationId : 신청서 고유 ID
                    ### 응답값
                    - code 200
                    - 승인처리 완료
                    - code 2000
                    - 로그인 된 사용자를 찾을 수 없는경우
                    - 신청서를 찾을 수 없는경우
                    - 승인하는 신청서의 포지션이 프로젝트에 없는경우
                    - 승인하는 신청서의 포지션이 이미 모집완료 되었을 경우
                    - 승인자와 프로젝트 생성자가 다른경우
                    """)
    ResponseEntity<Envelope<Void>> acceptProjectApplication(Long applicationId);
}
