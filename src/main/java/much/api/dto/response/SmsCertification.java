package much.api.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SmsCertification {

    private final String phoneNumber;

    private final int remainTimeInMinutes;

}
