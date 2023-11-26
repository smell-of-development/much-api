package much.api.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import much.api.common.exception.VerificationNumberNotMatched;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Entity
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "tb_sms_verification_hist",
        indexes = {
                @Index(name = "tb_sms_verification_hist_idx1", columnList = "phoneNumber")
        }
)
public class SmsVerificationHist extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String phoneNumber;

    private String number;

    @ColumnDefault("0")
    private boolean verified;


    public void verify(String verificationNumber) {

        if (!this.number.equals(verificationNumber)) {
            throw new VerificationNumberNotMatched(verificationNumber);
        }
        verified = true;
    }

    @Builder
    private SmsVerificationHist(String phoneNumber, String number, boolean verified) {
        this.phoneNumber = phoneNumber;
        this.number = number;
        this.verified = verified;
    }
}
