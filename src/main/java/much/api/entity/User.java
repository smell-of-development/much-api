package much.api.entity;

import jakarta.persistence.*;
import lombok.*;
import much.api.common.enums.Role;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Entity
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "tb_user",
        indexes = {
                @Index(name = "tb_user_idx1", columnList = "loginId"),
                @Index(name = "tb_user_idx2", columnList = "kakaoId"),
                @Index(name = "tb_user_idx3", columnList = "googleId"),
        }
)
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
    private String imageUrl;

    @Setter
    private String phoneNumber;

    private String email;

    private String name;

    private String nickname;

    private String position;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ColumnDefault("1")
    private boolean refreshable;

    @Builder
    private User(String kakaoId,
                String googleId,
                String loginId,
                String password,
                String imageUrl,
                String phoneNumber,
                String email,
                String name,
                String nickname,
                String position,
                Role role,
                boolean refreshable) {

        this.kakaoId = kakaoId;
        this.googleId = googleId;
        this.loginId = loginId;
        this.password = password;
        this.imageUrl = imageUrl;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.position = position;
        this.role = role;
        this.refreshable = refreshable;
    }
}
