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

    private String socialId;

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


    public boolean isNewUser() {

        return positionIds == null;
    }


}
