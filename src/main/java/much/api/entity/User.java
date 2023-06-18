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
@Table(name = "TB_USER")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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
    private String picture;

    @Setter
    private String phoneNumber;

    @Setter
    @ColumnDefault("0")
    private boolean phoneVerificationCompleted;

    private String email;

    private String name;

    private String nickname;

    @OneToOne
    private Position jobGroup;

    @OneToOne
    private Position career;

    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean refreshable;

}
