package much.api.common.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Code {

    // 개발시 확인용
    DEV_MESSAGE(1000, "%s"),
    DEV_INVALID_VALUE(1000, "요청값을 확인해주세요."),
    DEV_INVALID_PARAM_NAME(1000, "필수요청 파라미터 이름을 확인해주세요."),
    DEV_INVALID_PARAM_VALUE(1000, "필수요청 파라미터 값을 확인해주세요."),
    DEV_INCORRECT_FORMAT(1000, "요청메세지 형식이 잘못되었습니다."),

    // 사용자 노출예외 >>> 예외클래스와 1:1
    INCORRECT_LOGIN_INFO(2000, "ID 또는 비밀번호가 일치하지 않아요."),

    USER_NOT_FOUND(2000, "사용자를 찾을 수 없어요."),
    PHONE_NUMBER_NOT_FOUND(2000, "전화번호에 해당하는 사용자를 찾을 수 없어요."),
    POST_NOT_FOUND(2000, "게시글을 찾을 수 없어요."),
    PROJECT_NOT_FOUND(2000, "프로젝트 정보를 찾을 수 없어요."),
    FILE_NOT_FOUND(2000, "파일을 찾을 수 없어요."),

    INVALID_NICKNAME(2000, "닉네임은 2글자 이상 8글자 이하 완성된 한글, 영어, 숫자만 사용할 수 있어요."),
    INVALID_LOGIN_ID(2000, "ID는 4글자 이상 20글자 이하 영어, 숫자만 사용할 수 있어요."),
    INVALID_PASSWORD(2000, "비밀번호는 공백을 제외한 8글자 이상 20글자 이하만 가능해요."),
    INVALID_PHONE_NUMBER(2000, "휴대폰번호 형태가 아니에요."),
    INVALID_LENGTH(2000, "%s %s자 미만이어야 해요.(%s/%s)"),
    INVALID_PERIOD(2000, "일정 시작일이 종료일보다 같거나 빨라야해요."),
    INVALID_DEADLINE(2000, "모집 마감일을 확인해주세요."),
    INVALID_RECRUIT_NEEDS(2000, "각 포지션별 모집인원은 1명 이상이어야 해요."),

    DUPLICATED_NICKNAME(2000, "중복되는 닉네임이 있어 사용할 수 없어요."),
    DUPLICATED_LOGIN_ID(2000, "중복되는 로그인 ID가 있어 사용할 수 없어요."),
    DUPLICATED_PHONE_NUMBER(2000, "중복된 휴대폰번호에요."),

    MESSAGE_SENDING_FAIL(2000, "메세지 발송에 실패했어요."),
    CERTIFICATION_MESSAGE_SENDING_COUNT_EXCEEDED(2000, "하루 최대 전송횟수를 초과했어요."),
    CERTIFICATION_NUMBER_NOT_MATCHED(2000, "인증번호가 일치하지 않아요."),
    CERTIFICATION_NUMBER_SENDING_NEEDED(2000, "인증번호 전송이 필요해요."),
    CERTIFICATION_NEEDED(2000, "SMS 인증이 필요해요."),

    NOT_IMAGE_FILE(2000, "이미지 파일이 아니에요."),

    TOKEN_REFRESH_BLOCKED(4000, "토큰 리프레시가 차단되었어요."),
    UNAUTHORIZED(4001, "정상적인 로그인 정보가 없어요."),
    FORBIDDEN(4003, "권한이 없어요."),

    FILE_UPLOAD_SIZE_EXCEEDED(5000, "최대 업로드 크기를 초과해요."),

    // 서버
    HANDLER_NOT_FOUND(9000, "처리 가능한 핸들러를 찾을 수 없습니다."),
    METHOD_NOT_ALLOWED(9000, "허용된 메서드가 아닙니다."),

    FILE_PROCESS_ERROR(9000, "파일 처리중 오류"),

    INTERNAL_SERVER_ERROR(9999, "확인되지 않은 예외");

    private final Integer code;

    private final String message;

}
