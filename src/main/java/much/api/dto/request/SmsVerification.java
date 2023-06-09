package much.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SmsVerification {

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String certificationNumber;

}
