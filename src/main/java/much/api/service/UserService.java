package much.api.service;

import much.api.dto.Jwt;
import much.api.dto.request.JoinInformation;
import much.api.dto.response.Envelope;
import much.api.entity.User;

import java.util.Optional;

public interface UserService {

    Envelope<Jwt> registerUser(JoinInformation joinInformation);

    Envelope<Jwt> linkUser(String targetPhoneNumber, Long toDeletedId);

    Optional<User> findUserByPhoneNumber(String phoneNumber);
}
