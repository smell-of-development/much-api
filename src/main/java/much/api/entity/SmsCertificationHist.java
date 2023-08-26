package much.api.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Entity
@Builder
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(
        name = "TB_SMS_CERTIFICATION_HIST",
        indexes = {@Index(name = "IDX__PHONE_NUMBER", columnList = "phoneNumber")}
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

}
