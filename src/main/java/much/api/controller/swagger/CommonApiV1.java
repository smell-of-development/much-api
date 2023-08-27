package much.api.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import much.api.common.enums.Code;
import much.api.dto.response.Envelope;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@Tag(name = "공통 API")
public interface CommonApiV1 {

    @Operation(summary = "코드 목록 조회",
            description = """
                    서버에서 응답하는 모든 코드와 메세지들을 조회합니다.
                    ### 응답값 설명
                    - result[] : [{"id": integer, "message": "string"}, ...]
                    """)
    ResponseEntity<Envelope<Code[]>> retrieveCodes();

    @Operation(summary = "중복 로그인 ID 조회",
            description = """
                    로그인 ID 유효성 검사와 중복 닉네임 검사 API
                    ### 응답값 설명
                    - code 200  : 사용할 수 있는 로그인 ID
                    - code 1000 : 값 미존재
                    - code 2007 : ID는 4글자 이상 20글자 이하 영어, 숫자만 사용할 수 있습니다.
                    - code 2006 : 중복되는 로그인 ID가 있어 사용할 수 없습니다.
                    """)
    ResponseEntity<Envelope<Void>> retrieveDuplicatedLoginId(String id);

    @Operation(summary = "중복 닉네임 조회",
            description = """
                    닉네임 유효성 검사와 중복 닉네임 검사 API
                    ### 응답값 설명
                    - code 200  : 사용할 수 있는 닉네임
                    - code 1000 : 값 미존재
                    - code 2002 : 닉네임은 2글자 이상 8글자 이하 완성된 한글, 영어, 숫자만 사용할 수 있습니다.
                    - code 2003 : 중복되는 닉네임이 있어 사용할 수 없습니다.
                    """)
    ResponseEntity<Envelope<Void>> retrieveDuplicatedNickname(String nickname);

    @Operation(summary = "스킬태그 검색",
            description = """
                    스킬 태그목록을 검색합니다.
                    ### 요청
                    - 쿼리스트링 name=value : value 문자열이 포함되는 스킬태그 검색
                    - 영어와 순수한글 모두 지원합니다. ex) name=script | name=스크립트
                    ### 응답값 설명
                    - result[] : [string, ...]
                    """)
    ResponseEntity<Envelope<List<String>>> retrieveSkills(String name);

    @Operation(summary = "이미지 업로드",
            description = """
                    이미지를 업로드합니다.
                    ### 요청
                    - POST /common/image?type=thumbnail
                    - (multipart/form-data) image
                    - type 파라미터 종류 : thumbnail, profile, none
                    - type 파라미터 종류에 따라서 이미지 리사이징
                    ### 응답값 설명
                    - code 200  : "result": string(이미지 주소)
                    - code 9003 : 파일 업로드중 오류
                    - code 9004 : 이미지 파일이 아닙니다.
                    """)
    ResponseEntity<Envelope<String>> uploadImage(MultipartFile file, String type);

    @Operation(summary = "이미지 획득",
            description = """
                    업로드 된 이미지를 획득합니다.
                    ### 요청
                    - GET 이미지주소
                    ### 응답값
                    - 이미지 리소스
                    """)
    ResponseEntity<Resource> retrieveImage(String storedFilename) throws IOException;

}
