package much.api.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "TB_POSITION")
public class Position {

    public static final int REQUIRED_POSITION_SIZE = 2;

    @Id
    private Integer code;

    @Column(length = 50)
    private String name;

}
