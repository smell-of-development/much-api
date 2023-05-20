package much.api.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Code {

    INVALID_INPUT_VALUE(1000, "입력값이 잘못되었습니다."),
    INCORRECT_FORMAT(1100, "잘못된 형식입니다."),


    TOKEN_EXPIRE(4000, "액세스토큰 만료"),
    UNAUTHORIZED(4100, "정상적인 인증정보가 필요합니다."),
    FORBIDDEN(4300, "권한이 없습니다."),

    HANDLER_NOT_FOUND(4900, "처리 가능한 핸들러를 찾을 수 없습니다."),
    METHOD_NOT_ALLOWED(4910, "허용된 메서드가 아닙니다."),
    FILE_UPLOAD_SIZE_EXCEEDED(4999, "최대 업로드 크기를 초과합니다."),


    PHONE_NUMBER_DUPLICATED(8000, "휴대폰 번호 중복"),
    ADDITIONAL_INFORMATION_REQUIRED_1(8100, "추가정보 입력이 필요합니다."),
    ADDITIONAL_INFORMATION_REQUIRED_2(8101, "추가정보 입력과 휴대폰번호 인증이 필요합니다."),

    NOT_SUPPORTED_OAUTH2_PROVIDER(9000, "지원하지 않는 PROVIDER 입니다."),
    INTERNAL_SERVER_ERROR(9999, "Internal Server Error");

    private final Integer code;

    private final String message;

}
