package much.api.service;

import much.api.dto.response.Envelope;
import much.api.dto.response.Positions;

public interface CommonService {

    Envelope<Positions> retrievePositions();

    void checkDuplicatedNickname(String nickname);

}
