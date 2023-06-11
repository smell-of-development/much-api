package much.api.common.util;

import java.util.regex.Pattern;

public class ValidationChecker {

    private static final String PHONE_NUMBER_REGEX = "(\\d{3})-?(\\d{4})-?(\\d{4})$";

    private static final String LOGIN_ID_REGEX = "[a-zA-Z0-9]{4,20}$";

    private static final String PASSWORD_REGEX = "\\S{8,20}$";

    private static final String NICKNAME_REGEX = "[가-힣a-zA-Z0-9]{2,8}$";

    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile(PHONE_NUMBER_REGEX);

    private static final Pattern LOGIN_ID_PATTERN = Pattern.compile(LOGIN_ID_REGEX);

    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);

    private static final Pattern NICKNAME_PATTERN = Pattern.compile(NICKNAME_REGEX);


    public static boolean isValidLoginId(String id) {

        return LOGIN_ID_PATTERN.matcher(id).matches();
    }


    public static boolean isValidPassword(String password) {

        return PASSWORD_PATTERN.matcher(password).matches();
    }


    public static boolean isValidNickname(String nickname) {

        return NICKNAME_PATTERN.matcher(nickname).matches();
    }


    public static boolean isValidPhoneNumber(String phoneNumber) {

        return PHONE_NUMBER_PATTERN.matcher(phoneNumber).matches();
    }

}
