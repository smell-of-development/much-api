package much.api.dto.request;

import lombok.Getter;

@Getter
public class SmsVerification {

    private Long id;

    private String phoneNumber;

    private String certificationNumber;

}
