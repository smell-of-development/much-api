package much.api.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Getter
public class Positions {
    private final List<Position> positions;


    public Positions(List<Position> positions) {
        this.positions = positions;
    }

    @Getter
    @Builder(access = AccessLevel.PRIVATE)
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Position {

        private final Integer code;

        private final Integer parentCode;

        private final String name;

        @Schema(description = "하위 분류", example = "[]")
        private final List<Position> children;

        public static Position of(much.api.entity.Position entity) {
            if (entity == null) return null;

            return builder()
                    .code(entity.getCode())
                    .name(entity.getName())
                    .parentCode(entity.getParent() == null ? null : entity.getParent().getCode())
                    .children(entity.getChildren()
                            .stream()
                            .map(Position::of)
                            .collect(collectingAndThen(Collectors.toList(), c -> c.isEmpty() ? null : c)))
                    .build();
        }

    }

}
