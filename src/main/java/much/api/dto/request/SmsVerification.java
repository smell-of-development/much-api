package much.api.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class SmsVerification {

    @NotNull
    private String phoneNumber;

    @NotNull
    private String certificationNumber;

}
