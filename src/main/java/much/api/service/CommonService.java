package much.api.service;

import lombok.RequiredArgsConstructor;
import much.api.entity.User;
import much.api.exception.DuplicatedLoginID;
import much.api.exception.DuplicatedNickname;
import much.api.exception.DuplicatedPhoneNumber;
import much.api.exception.UserNotFound;
import much.api.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommonService {

    private final UserRepository userRepository;


    public User getUserOrThrowException(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFound(id));
    }


    public void checkDuplicatedLoginId(String loginId) {

        if (userRepository.existsByLoginId(loginId)) {
            throw new DuplicatedLoginID(loginId);
        }
    }


    public void checkDuplicatedNickname(String nickname) {

        if (userRepository.existsByNickname(nickname)) {
            throw new DuplicatedNickname(nickname);
        }

    }


    public void checkDuplicatedPhoneNumber(String phoneNumber) {

        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new DuplicatedPhoneNumber(phoneNumber);
        }
    }

}
