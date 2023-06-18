package much.api.service;

import much.api.dto.request.MuchRegistration;
import much.api.dto.response.Envelope;

public interface MuchService {

    Envelope<Void> registerMuch(MuchRegistration registration);

}
