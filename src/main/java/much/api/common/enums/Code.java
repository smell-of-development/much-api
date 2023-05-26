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
    INVALID_NICKNAME(2002, "닉네임은 2글자 이상 완성된 한글, 영어, 숫자만 사용할 수 있습니다."),
    DUPLICATED_NICKNAME(2003, "중복되는 닉네임이 있어 사용할 수 없습니다."),

    UNAUTHORIZED(4000, "정상적인 토큰이 필요합니다."),
    FORBIDDEN(4100, "권한이 없습니다."),

    DUPLICATED_PHONE_NUMBER(8000, "휴대폰 번호 중복"),
    NOT_MATCHED_PHONE_NUMBER_PATTERN(8001, "휴대폰번호 형태가 아닙니다."),
    ADDITIONAL_INFORMATION_REQUIRED_1(8100, "추가정보 입력이 필요합니다."),
    ADDITIONAL_INFORMATION_REQUIRED_2(8101, "추가정보 입력과 휴대폰번호 인증이 필요합니다."),
    NOT_SUPPORTED_OAUTH2_PROVIDER(8999, "지원하지 않는 PROVIDER 입니다."),

    HANDLER_NOT_FOUND(9000, "처리 가능한 핸들러를 찾을 수 없습니다."),
    METHOD_NOT_ALLOWED(9001, "허용된 메서드가 아닙니다."),
    FILE_UPLOAD_SIZE_EXCEEDED(9002, "최대 업로드 크기를 초과합니다."),

    INTERNAL_SERVER_ERROR(9999, "확인되지 않은 예외");

    private final Integer code;

    private final String message;

}
