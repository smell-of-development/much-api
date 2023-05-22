package much.api.entity;

import jakarta.persistence.*;
import lombok.*;
import much.api.common.enums.OAuth2Provider;
import much.api.common.enums.Role;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import static much.api.common.enums.OAuth2Provider.*;

@Getter
@Entity
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "TB_USER")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String kakaoId;

    private String googleId;

    @Enumerated(EnumType.STRING)
    private OAuth2Provider firstLinkedSocial;

    @Setter
    private String picture;

    @Setter
    @Column(length = 20)
    private String phoneNumber;

    @Column(length = 100)
    private String email;

    @Column(length = 40)
    private String name;

    @Column(length = 8)
    private String nickname;

    private String positionIds;

    private String positionClass;

    @Column(length = 20)
    @ColumnDefault("'ROLE_USER'")
    @Enumerated(EnumType.STRING)
    private Role role;

    @ColumnDefault("1")
    private Boolean refreshable;

    @Builder
    private User(String kakaoId,
                 String googleId,
                 String picture,
                 String phoneNumber,
                 String email,
                 String name,
                 String nickname,
                 String positionIds,
                 String positionClass) {

        this.kakaoId = kakaoId;
        this.googleId = googleId;
        this.firstLinkedSocial = kakaoId != null ? KAKAO : GOOGLE;
        this.picture = picture;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.positionIds = positionIds;
        this.positionClass = positionClass;
    }


    public boolean isNewUser() {

        return positionIds == null;
    }


}
