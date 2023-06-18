package much.api.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class Positions {

    private final List<Position> jobGroups;

    private final List<Position> careers;

    @Getter
    @Builder(access = AccessLevel.PRIVATE)
    @RequiredArgsConstructor(access = AccessLevel.PUBLIC)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Position {

        private final Integer code;

        private final String name;

    }

}
