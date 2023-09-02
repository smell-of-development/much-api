package much.api.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import much.api.dto.request.CommunityPostCreation;
import much.api.dto.request.CommunityPostModification;
import much.api.dto.response.CommunityPostDetail;
import much.api.dto.response.Envelope;
import org.springframework.http.ResponseEntity;

@Tag(name = "커뮤니티 API", description = "커뮤니티 관련 API")
public interface CommunityApiV1 {

    @Operation(
            summary = "커뮤니티 글 등록 API",
            description = """
                    커뮤니티 글을 등록합니다.
                    - 로그인이 되어있어야 합니다.
                    ### 요청값
                    - category (String)   : (필수) QNA, FREE, TECH_SHARE 중 하나
                    - tags     (String[]) : 태그 배열
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
