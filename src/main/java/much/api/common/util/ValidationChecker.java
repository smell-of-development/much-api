package much.api.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import much.api.exception.InvalidLoginID;
import much.api.exception.InvalidNickname;
import much.api.exception.InvalidPassword;
import much.api.exception.InvalidPhoneNumber;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidationChecker {

    private static final String PHONE_NUMBER_REGEX = "(010)(\\d{4})(\\d{4})$";
    private static final String HYPHEN_PHONE_NUMBER_REGEX_WITH = "(010)-(\\d{4})-(\\d{4})$";
    private static final String LOGIN_ID_REGEX = "[a-zA-Z0-9]{4,20}$";
    private static final String PASSWORD_REGEX = "\\S{8,20}$";
    private static final String NICKNAME_REGEX = "[가-힣a-zA-Z0-9]{2,8}$";

    public static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile(PHONE_NUMBER_REGEX);
    public static final Pattern HYPHEN_PHONE_NUMBER_PATTERN = Pattern.compile(HYPHEN_PHONE_NUMBER_REGEX_WITH);
    public static final Pattern LOGIN_ID_PATTERN = Pattern.compile(LOGIN_ID_REGEX);
    public static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);
    public static final Pattern NICKNAME_PATTERN = Pattern.compile(NICKNAME_REGEX);


    public static void isValidLoginId(String id) {

        if (!StringUtils.hasText(id) || !LOGIN_ID_PATTERN.matcher(id).matches()) {
            throw new InvalidLoginID(id);
        }
    }


    public static void isValidPassword(String password) {

        if (!StringUtils.hasText(password) || !PASSWORD_PATTERN.matcher(password).matches()) {
            throw new InvalidPassword();
        }
    }


    public static void isValidNickname(String nickname) {

        if (!StringUtils.hasText(nickname) || !NICKNAME_PATTERN.matcher(nickname).matches()) {
            throw new InvalidNickname(nickname);
        }
    }


    public static void isValidPhoneNumber(String phoneNumber) {

        if (!StringUtils.hasText(phoneNumber) || !PHONE_NUMBER_PATTERN.matcher(phoneNumber).matches()) {
            throw new InvalidPhoneNumber(phoneNumber);
        }
    }

}
