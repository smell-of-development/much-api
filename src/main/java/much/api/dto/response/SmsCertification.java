package much.api.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SmsCertification {

    private String phoneNumber;

    private int remainTimeInMinutes;

    @Builder
    private SmsCertification(String phoneNumber, int remainTimeInMinutes) {
        this.phoneNumber = phoneNumber;
        this.remainTimeInMinutes = remainTimeInMinutes;
    }

}
