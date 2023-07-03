package much.api.common.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Code {

    INVALID_VALUE_FOR(1000, "요청값 [%s]을 확인해주세요."),
    REQUIRED_INFORMATION(1001, "필수 정보입니다."),
    INCORRECT_FORMAT(1100, "잘못된 메세지 형식입니다."),

    USER_NOT_FOUND(2000, "사용자를 찾을 수 없습니다."),
    TOKEN_REFRESH_BLOCKED_USER(2001, "토큰 리프레시가 차단된 사용자입니다."),
    INVALID_NICKNAME(2002, "닉네임은 2글자 이상 8글자 이하 완성된 한글, 영어, 숫자만 사용할 수 있습니다."),
    DUPLICATED_NICKNAME(2003, "중복되는 닉네임이 있어 사용할 수 없습니다."),
    USER_NOT_FOUND_FOR_PHONE_NUMBER(2004, "전화번호에 해당하는 사용자를 찾을 수 없습니다."),
    INCORRECT_PASSWORD(2005, "일치하지 않는 비밀번호 입니다."),
    DUPLICATED_LOGIN_ID(2006, "중복되는 로그인 ID가 있어 사용할 수 없습니다."),
    INVALID_LOGIN_ID(2007, "ID는 4글자 이상 20글자 이하 영어, 숫자만 사용할 수 있습니다."),
    INVALID_PASSWORD(2008, "비밀번호는 공백을 제외한 8글자 이상 20글자 이하만 가능합니다."),

    UNAUTHORIZED(4000, "정상적인 토큰이 필요합니다."),
    FORBIDDEN(4100, "권한이 없습니다."),

    PROJECT_NOT_FOUND(5000, "프로젝트 정보를 찾을 수 없습니다."),

    DUPLICATED_PHONE_NUMBER(8000, "휴대폰 번호 중복"),
    NOT_MATCHED_PHONE_NUMBER_PATTERN(8001, "휴대폰번호 형태가 아닙니다."),
    MESSAGE_SENDING_FAIL(8002, "메세지 발송에 실패하였습니다."),
    CERTIFICATION_NUMBER_NOT_MATCHED(8003, "인증번호가 일치하지 않습니다."),
    SENDING_HISTORY_NOT_FOUND(8004, "인증번호 전송기록을 찾을 수 없습니다."),

    POSITION_CODE_NOT_FOUNT(8100, "입력된 포지션 코드를 찾을 수 없습니다."),

    NOT_SUPPORTED_OAUTH2_PROVIDER(8999, "지원하지 않는 PROVIDER 입니다."),

    HANDLER_NOT_FOUND(9000, "처리 가능한 핸들러를 찾을 수 없습니다."),
    METHOD_NOT_ALLOWED(9001, "허용된 메서드가 아닙니다."),
    FILE_UPLOAD_SIZE_EXCEEDED(9002, "최대 업로드 크기를 초과합니다."),
    FILE_PROCESS_ERROR(9003, "파일 처리중 오류"),
    NOT_IMAGE_FILE(9004, "이미지 파일이 아닙니다."),

    INTERNAL_SERVER_ERROR(9999, "확인되지 않은 예외");

    private final Integer code;

    private final String message;

}
