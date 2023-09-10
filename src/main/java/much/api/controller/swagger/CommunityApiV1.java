package much.api.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import much.api.dto.request.CommunityPostCreation;
import much.api.dto.request.CommunityPostModification;
import much.api.dto.request.CommunitySearch;
import much.api.dto.response.*;
import org.springframework.http.ResponseEntity;

@Tag(name = "커뮤니티 API", description = "커뮤니티 관련 API")
public interface CommunityApiV1 {

    @Operation(
            summary = "커뮤니티 글 단건조회 API",
            description = """
                    커뮤니티 글 ID로 단건조회합니다.
                    - 요청예시 GET /api/v1/communities/1
                    ### 요청값
                    - 경로변수(postId) : (필수) 게시글 ID
                    ### 응답값
                    - code 200 : 정상조회
                    """)
    ResponseEntity<Envelope<CommunityPostDetail>> getPost(Long id);

    @Operation(
            summary = "커뮤니티 글 다건조회 API",
            description = """
                    커뮤니티 글을 다건 조회합니다.
                    - 페이지 결과를 얻습니다.
                    - 내용은 50자까지 미리보기
                    - 요청예시 GET /api/v1/communities?category=QNA&search=제목또는내용&page=1&size=10
                    ### 정확도순 정렬시(byRecent=false) 우선순위
                    - search + tags 모두 존재   : 1) 태그 일치 개수, 2) 검색어 정확도, 3) 등록순서
                    - search 존재, tags 미존재  : 1) 검색어 정확도,  2) 등록순서
                    - search 미존재, tags 존재  : 1) 태그 일치 개수, 2) 등록순서
                    - search + tags 모두 미존재 : 검색하지 않음(비어있는 결과 리스트)
                    ### 요청값
                    - 쿼리스트링(category) - String  : (필수) 게시글 카테고리 QNA, FREE, TECH_SHARE 중 하나
                    - 쿼리스트링(search)   - String  : (선택) 제목 또는 내용 검색어
                    - 쿼리스트링(tags)     - String  : (선택) 검색할 태그들을 "," 으로 이어붙인 문자열
                    - 쿼리스트링(page)     - String  : (선택) 검색할 페이지. 기본값 1
                    - 쿼리스트링(size)     - String  : (선택) 검색할 페이지당 사이즈. 기본값 10, 최대 40
                    - 쿼리스트링(byRecent) - Boolean : (선택) true - 최신순 정렬, false - 정확도순 정렬. 기본값 true
                    ### 응답값
                    - code 200 : 정상조회
                    - result.elements[]       : 페이징 된 결과들
                    - result.page             : 현재 페이지
                    - result.numberOfElements : 현재 페이지 요소들의 수
                    - result.totalPages       : 전체 페이지
                    - result.first            : 첫번째 페이지인지 여부 true/false
                    - result.last             : 마지막 페이지인지 여부 true/false
                    """)
    ResponseEntity<Envelope<PagedResult<CommunityPostSummary>>> getPosts(CommunitySearch searchCondition);

    @Operation(
            summary = "커뮤니티 글 등록 API",
            description = """
                    커뮤니티 글을 등록합니다.
                    - 로그인이 되어있어야 합니다.
                    ### 요청값
                    - category (String)   : (필수) QNA, FREE, TECH_SHARE 중 하나
                    - tags     (String[]) : 태그 배열
                    - title    (String)   : (필수) 글 제목
                    - content  (String)   : 에디터 내용
                    ### 응답값
                    - code 200
                    - 글 ID, 본인글여부, 카테고리, 태그배열, 글 내용, 작성자 ID, 작성자 닉네임, 작성자 프로필 이미지 url
                    - code 2000
                    - 로그인 된 사용자를 찾을 수 없는경우
                    """,
            requestBody = @RequestBody(required = true, description = "카테고리, 태그, 내용"))
    ResponseEntity<Envelope<CommunityPostDetail>> createCommunityPost(CommunityPostCreation request);

    @Operation(
            summary = "커뮤니티 글 수정 API",
            description = """
                    커뮤니티 글을 수정합니다.
                    - 로그인 사용자와 등록자가 같아야합니다.
                    - 요청예시 PUT /api/v1/communities/1
                    ### 요청값
                    - 쿼리스트링(id) - Number   : (필수) 글 ID
                    - body(category) - String   : (필수) QNA, FREE, TECH_SHARE 중 하나 (수정 데이터)
                    - body(tags)     - String[] : 태그 배열 (수정 데이터)
                    - body(title)    - String   : (필수) 글 제목
                    - body(content)  - String   : 에디터 내용 (수정 데이터)
                    ### 응답값
                    - code 200
                    - 글 ID, 본인글여부, 카테고리, 태그배열, 글 내용, 작성자 ID, 작성자 닉네임, 작성자 프로필 이미지 url
                    - code 2000
                    - 로그인 된 사용자를 찾을 수 없는경우
                    - 본인글이 아닌경우
                    """,
            requestBody = @RequestBody(required = true, description = "카테고리, 태그, 내용"))
    ResponseEntity<Envelope<CommunityPostDetail>> modifyCommunityPost(Long postId,
                                                                      CommunityPostModification request);

    @Operation(
            summary = "커뮤니티 글 삭제 API",
            description = """
                    커뮤니티 글을 삭제합니다.
                    - 로그인 사용자와 등록자가 같아야합니다.
                    - 요청예시 DELETE /api/v1/communities/1
                    ### 응답값
                    - code 200
                    - 삭제 성공
                    - code 2000
                    - 로그인 된 사용자를 찾을 수 없는경우
                    - 본인글이 아닌경우
                    """,
            requestBody = @RequestBody(required = true, description = "카테고리, 태그, 내용"))
    ResponseEntity<Envelope<Void>> deleteCommunityPost(Long postId);
}
