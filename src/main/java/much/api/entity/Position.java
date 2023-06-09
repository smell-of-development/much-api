package much.api.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "TB_POSITION")
public class Position {

    public static final int REQUIRED_POSITION_SIZE = 2;

    @Id
    private Integer code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_code")
    private Position parent;

    @Column(length = 50)
    private String name;

    @OneToMany(mappedBy = "parent")
    private List<Position> children;

}
