package much.api.entity;

import jakarta.persistence.*;
import lombok.*;
import much.api.common.enums.Role;
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
@Table(name = "TB_USER")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private String kakaoId;

    @Setter
    private String googleId;

    private String loginId;

    private String password;

    @Setter
    private String pictureUrl;

    @Setter
    private String phoneNumber;

    @Setter
    @ColumnDefault("0")
    private boolean phoneVerificationCompleted;

    private String email;

    private String name;

    private String nickname;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_group_code")
    private Position jobGroup;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "career_code")
    private Position career;

    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean refreshable;

}
