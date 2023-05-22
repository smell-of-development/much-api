package much.api.common.util;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import lombok.extern.slf4j.Slf4j;

import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class PhoneNumberUtils {

    private static final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

    private static final String PHONE_NUMBER_REGEX = "(\\d{3})-?(\\d{4})-?(\\d{4})$";

    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile(PHONE_NUMBER_REGEX);

    private static final String MASK = "****";


    public static Optional<String> toHyphenFormat(String phoneNumber) {

        return toNational(phoneNumber);
    }


    public static Optional<String> toMaskedHyphenFormat(String phoneNumber) {

        return toNational(phoneNumber)
                .map(PhoneNumberUtils::masking);
    }


    public static Optional<String> toOnlyDigits(String phoneNumber) {

        return toNational(phoneNumber)
                .map(PhoneNumberUtil::normalizeDigitsOnly);
    }


    private static Optional<String> toNational(String phoneNumber) {

        try {
            Phonenumber.PhoneNumber toParsed = phoneNumberUtil.parse(phoneNumber, Locale.KOREA.getCountry());

            String format = phoneNumberUtil.format(toParsed, PhoneNumberUtil.PhoneNumberFormat.NATIONAL);
            if (format.matches(PHONE_NUMBER_REGEX)) {
                return Optional.of(format);
            }

        } catch (Exception e) {
            log.debug("phoneNumber 파싱중 예외", e);
        }
        return Optional.empty();
    }


    private static String masking(String phoneNumber) {

        Matcher matcher = PHONE_NUMBER_PATTERN.matcher(phoneNumber);
        if (matcher.find()) {
            String middle = matcher.group(2);
            return phoneNumber.replace(middle, MASK);
        }

        return null;
    }

}
