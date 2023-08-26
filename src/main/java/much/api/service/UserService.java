package much.api.service;

import much.api.dto.response.WebToken;
import much.api.dto.request.JoinInformation;
import much.api.dto.response.Envelope;
import much.api.entity.User;

import java.util.Optional;

public interface UserService {

    Envelope<WebToken> registerUser(JoinInformation joinInformation);

    Envelope<WebToken> linkUser(String targetPhoneNumber, Long toDeletedId);

    Optional<User> findUserByPhoneNumber(String phoneNumber);

    void checkDuplicatedLoginId(String id);

    void checkDuplicatedNickname(String nickname);
}
