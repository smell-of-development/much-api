package much.api.service;

import much.api.common.enums.MuchType;
import much.api.dto.request.MuchRegistration;
import much.api.dto.response.Envelope;
import much.api.dto.response.MuchDetail;

public interface MuchService {

    Envelope<Long> registerMuch(MuchRegistration registration, MuchType type);

    Envelope<MuchDetail> retrieveProject(Long id);

}
