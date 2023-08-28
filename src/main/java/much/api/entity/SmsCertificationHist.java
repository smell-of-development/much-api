package much.api.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Entity
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "tb_sms_certification_hist",
        indexes = {
                @Index(name = "tb_sms_certification_hist_idx1", columnList = "phoneNumber")
        }
)
public class SmsCertificationHist extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String phoneNumber;

    private String number;

    @ColumnDefault("0")
    private boolean certified;


    public void certify() {

        certified = true;
    }

    @Builder
    private SmsCertificationHist(String phoneNumber, String number, boolean certified) {
        this.phoneNumber = phoneNumber;
        this.number = number;
        this.certified = certified;
    }
}
