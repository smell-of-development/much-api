package much.api.service;

import much.api.dto.response.Envelope;
import much.api.dto.response.PositionResponse;

public interface CommonService {

    Envelope<PositionResponse> retrievePositions();

}
