package much.api.common.util;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;

import static much.api.common.util.ValidationChecker.HYPHEN_PHONE_NUMBER_PATTERN;
import static much.api.common.util.ValidationChecker.PHONE_NUMBER_PATTERN;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PhoneNumberUtils {

    private static final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
    private static final String MASK = "****";


    public static boolean isOnlyDigitsPattern(String phoneNumber) {

        return PHONE_NUMBER_PATTERN.matcher(phoneNumber).matches();
    }

    public static boolean isHyphenPattern(String phoneNumber) {

        return HYPHEN_PHONE_NUMBER_PATTERN.matcher(phoneNumber).matches();
    }

    public static Optional<String> toHyphenFormat(String phoneNumber, boolean masked) {

        String hyphenPhoneNumber = toNational(phoneNumber);
        if (hyphenPhoneNumber == null) {
            return Optional.empty();
        }

        return masked ?
                Optional.ofNullable(masking(hyphenPhoneNumber))
                : Optional.of(hyphenPhoneNumber);
    }


    public static Optional<String> toOnlyDigitsFormat(String phoneNumber, boolean masked) {

        String hyphenPhoneNumber = toNational(phoneNumber);
        if (hyphenPhoneNumber == null) {
            return Optional.empty();
        }

        String digitsFormat = hyphenPhoneNumber.replace("-", "");
        return masked ?
                Optional.ofNullable(masking(digitsFormat))
                : Optional.of(digitsFormat);
    }


    /**
     * 입력된 휴대폰번호를 검사하고, 하이픈이 포함된 번호로 변환
     *
     * @param phoneNumber 여러가지 형식의 휴대폰번호
     * @return 010-####-@@@@ 형태의 휴대폰번호 문자열. 변환불가시 null
     */
    private static String toNational(String phoneNumber) {

        try {
            Phonenumber.PhoneNumber toParsed = phoneNumberUtil.parse(phoneNumber, Locale.KOREA.getCountry());

            String format = phoneNumberUtil.format(toParsed, PhoneNumberUtil.PhoneNumberFormat.NATIONAL);
            if (HYPHEN_PHONE_NUMBER_PATTERN.matcher(format).matches()) {
                return format;
            }

        } catch (Exception e) {
            log.debug("phoneNumber 파싱중 예외", e);
        }

        return null;
    }


    private static String masking(String phoneNumber) {

        Matcher matcher = null;

        if (isOnlyDigitsPattern(phoneNumber)) {
            matcher = PHONE_NUMBER_PATTERN.matcher(phoneNumber);
        }
        if (isHyphenPattern(phoneNumber)) {
            matcher = HYPHEN_PHONE_NUMBER_PATTERN.matcher(phoneNumber);
        }

        if (matcher == null) {
            return null;
        }

        if (matcher.find()) {
            String middle = matcher.group(2);
            return phoneNumber.replace(middle, MASK);
        }

        return null;
    }

}
