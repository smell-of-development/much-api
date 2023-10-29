package much.api.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import much.api.dto.request.StudyForm;
import much.api.dto.request.StudySearch;
import much.api.dto.response.Envelope;
import much.api.dto.response.PagedResult;
import much.api.dto.response.StudyDetail;
import much.api.dto.response.StudySummary;
import org.springframework.http.ResponseEntity;

@Tag(name = "스터디 관련 API")
public interface StudyApiV1 {

    @Operation(
            summary = "스터디 등록 API",
            description = """
                    스터디를 등록합니다.
                    - 로그인이 되어있어야 합니다.
                    ### 요청값
                    - title (String)         : 스터디 제목 (40자 이하) - 필수
                    - imageUrl (String)      : 대표 이미지 url
                    - online (Boolean)       : 온라인 모임 여부 - 필수
                    - address (String)       : 모임 위치 주소 / 값이 없다면 "협의"
                    - deadline (String)      : 모집 마감일 ex) 2023.11.15 (yyyy.MM.dd) - 필수
                    - startDate (String)     : 시작일 ex) 2023.09.10 (yyyy.MM.dd) / 시작일,종료일 모두 없어야 "협의"
                    - endDate (String)       : 종료일 ex) 2024.01.10 (yyyy.MM.dd) / 시작일,종료일 모두 없어야 "협의"
                    - meetingDays (String[]) : 모임일 ex) ["월", "수", "금"] / 값이 없다면 "협의"
                    - needs (Number)         : 모집인원 - 필수
                    - tags (String[])        : 스킬태그 ex) ["Java", "Spring", ...]
                    - introduction (String)  : 스터디 소개
                    ### 응답값
                    - code 200
                    - id (number) : 스터디 글 ID
                    - code 2000
                    - 로그인 된 사용자 미존재
                    """)
    ResponseEntity<Envelope<Long>> createStudy(StudyForm request);


    @Operation(
            summary = "스터디 수정 API",
            description = """
                    스터디 글을 수정합니다.
                    - 로그인 사용자와 등록자가 같아야합니다.
                    - 요청예시 PUT /v1/studies/1
                    ### 요청값
                    - title (String)         : 스터디 제목 (40자 이하) - 필수
                    - imageUrl (String)      : 대표 이미지 url
                    - online (Boolean)       : 온라인 모임 여부 - 필수
                    - address (String)       : 모임 위치 주소 / 값이 없다면 "협의"
                    - deadline (String)      : 모집 마감일 ex) 2023.11.15 (yyyy.MM.dd) - 필수
                    - startDate (String)     : 시작일 ex) 2023.09.10 (yyyy.MM.dd) / 시작일,종료일 모두 없어야 "협의"
                    - endDate (String)       : 종료일 ex) 2024.01.10 (yyyy.MM.dd) / 시작일,종료일 모두 없어야 "협의"
                    - meetingDays (String[]) : 모임일 ex) ["월", "수", "금"] / 값이 없다면 "협의"
                    - needs (Number)         : 모집인원 - 필수
                    - tags (String[])        : 스킬태그 ex) ["Java", "Spring", ...]
                    - introduction (String)  : 스터디 소개
                    ### 응답값
                    - code 200
                    - id (number) : 스터디 글 ID
                    - code 2000
                    - 로그인 된 사용자를 찾을 수 없는경우
                    - 본인글이 아닌경우
                    """)
    ResponseEntity<Envelope<Long>> modifyStudy(Long studyId,
                                               StudyForm request);


    @Operation(
            summary = "스터디 상세 조회 API",
            description = """
                    id 에 해당하는 스터디의 상세정보를 조회합니다.
                    ### 요청
                    - (경로변수) id : 스터디 고유 ID
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
                    - recruited      : 모집된 전체 인원 (Number)
                    - tags           : 태그 (String[])
                    - introduction   : 소개 (String)
                    ##### Object: writer
                    - id       : 작성자 고유 ID
                    - nickname : 작성자 닉네임
                    - imageUrl : 작성자 이미지
                    ##### Object: recruit
                    - state             : 모집 상태 ({"code": String, "name": String})
                    - needs             : 필요 인원 (Number)
                    - recruited         : 모집된 전체 인원 (Number)
                    """)
    ResponseEntity<Envelope<StudyDetail>> getStudy(Long studyId);


    @Operation(
            summary = "스터디 다건조회 API",
            description = """
                    # 검색기능에서 태그검색만 구현되어 있고, 검색어(search) 값 제목검색은 구현되어있지 않습니다.  
                    스터디를 다건 조회합니다.
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
                    - state        : 전체 모집 상태 ({"code": String, "name": String})
                    - needs        : 전체 필요 인원 (Number)
                    - recruited    : 모집된 전체 인원 (Number)
                    """)
    ResponseEntity<Envelope<PagedResult<StudySummary>>> getStudies(StudySearch searchCondition);
}
