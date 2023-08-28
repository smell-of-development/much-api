package much.api.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SmsVerification {

    @NotNull
    private String phoneNumber;

    @NotNull
    private String certificationNumber;

    @Builder
    private SmsVerification(String phoneNumber,
                            String certificationNumber) {

        this.phoneNumber = phoneNumber;
        this.certificationNumber = certificationNumber;
    }
}
