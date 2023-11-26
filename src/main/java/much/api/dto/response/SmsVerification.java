package much.api.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SmsVerification {

    private String phoneNumber;

    private int remainTimeInMinutes;

    @Builder
    private SmsVerification(String phoneNumber, int remainTimeInMinutes) {
        this.phoneNumber = phoneNumber;
        this.remainTimeInMinutes = remainTimeInMinutes;
    }

}
