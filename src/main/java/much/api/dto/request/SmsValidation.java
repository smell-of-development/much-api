package much.api.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SmsValidation {

    @NotNull
    private String phoneNumber;

    @NotNull
    private String verificationNumber;

    @Builder
    private SmsValidation(String phoneNumber,
                          String verificationNumber) {

        this.phoneNumber = phoneNumber;
        this.verificationNumber = verificationNumber;
    }
}
