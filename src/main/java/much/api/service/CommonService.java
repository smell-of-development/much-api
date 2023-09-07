package much.api.service;

import lombok.RequiredArgsConstructor;
import much.api.common.util.ContextUtils;
import much.api.entity.DevParameter;
import much.api.entity.User;
import much.api.common.exception.DuplicatedLoginID;
import much.api.common.exception.DuplicatedNickname;
import much.api.common.exception.DuplicatedPhoneNumber;
import much.api.common.exception.UserNotFound;
import much.api.repository.DevParameterRepository;
import much.api.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommonService {

    private final UserRepository userRepository;

    private final DevParameterRepository devParameterRepository;


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


    public boolean isSmsPass() {

        if (ContextUtils.isDevMode()) {

            Optional<DevParameter> optional = devParameterRepository.findByName("SMS_PASS");
            return optional.isPresent() && optional.get().isUseYn();
        }

        return false;
    }

}
