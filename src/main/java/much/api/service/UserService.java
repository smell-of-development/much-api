package much.api.service;

import lombok.RequiredArgsConstructor;
import much.api.common.enums.Role;
import much.api.common.util.ContextUtils;
import much.api.common.util.PhoneNumberUtils;
import much.api.common.util.TokenProvider;
import much.api.dto.MuchValid;
import much.api.dto.request.UserCreation;
import much.api.dto.response.WebToken;
import much.api.entity.SmsCertificationHist;
import much.api.entity.User;
import much.api.exception.*;
import much.api.repository.SmsCertificationHistRepository;
import much.api.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    private final SmsCertificationHistRepository smsCertificationHistRepository;

    private final PasswordEncoder passwordEncoder;

    private final TokenProvider tokenProvider;


    @Transactional
    public WebToken createUser(@MuchValid UserCreation userCreation) {

        // 로그인 ID 중복체크
        checkDuplicatedLoginId(userCreation.getLoginId());

        // 닉네임 중복체크
        checkDuplicatedNickname(userCreation.getNickname());

        final String phoneNumber = userCreation.getPhoneNumber();

        // 이미 가입된 휴대폰번호인지 검사
        if (userRepository.findByPhoneNumber(phoneNumber).isPresent()) {
            throw new DuplicatedPhoneNumber(phoneNumber);
        }

        // 휴대폰인증 SELECT - 1시간 이내
        LocalDateTime after = LocalDateTime.now().minusHours(1L);
        Optional<SmsCertificationHist> histOptional = smsCertificationHistRepository.findLatestSent(phoneNumber, after);

        // 개발환경 + 프로파일 smsPass 가 true 라면 인증검사 패스
        boolean certificationCompleted = ContextUtils.isSmsPass();

        // 인증이 완료된 번호인지 확인
        if (!certificationCompleted && histOptional.isPresent()) {

            SmsCertificationHist hist = histOptional.get();
            if (hist.isCertified()) {
                certificationCompleted = true;
            }
        }

        if (!certificationCompleted) {
            throw new CertificationNeeded(phoneNumber);
        }

        // 유저 등록
        User user = User.builder()
                .loginId(userCreation.getLoginId())
                .password(passwordEncoder.encode(userCreation.getPassword()))
                .nickname(userCreation.getNickname())
                .phoneNumber(phoneNumber)
                .role(Role.ROLE_USER)
                .position(userCreation.getPosition())
                .refreshable(true)
                .build();

        userRepository.save(user);

        TokenProvider.Jwt jwt = tokenProvider.createTokenResponse(user.getId(), user.getRole());
        return WebToken.ofJwt(jwt);
    }


    @Transactional
    public WebToken linkUser(String targetPhoneNumber, Long toDeletedId) {

        // 사용자 확인
        final User toBeDeletedUser = userRepository.findById(toDeletedId)
                .orElseThrow(() -> new UserNotFound(toDeletedId));

        // 휴대폰 인증이 완료된 다른 멀쩡한 사용자와 연동되는것 방지
//        if (toBeDeletedUser.isPhoneVerificationCompleted()) {
//
//        }

        // 휴대폰번호 형식 검사
        if (!PhoneNumberUtils.isOnlyDigitsPattern(targetPhoneNumber)) {
            throw new InvalidPhoneNumber(targetPhoneNumber);
        }

        // 휴대폰번호 사용자
//        final User targetUser = userRepository.findByPhoneNumber(targetPhoneNumber)
//                .orElseThrow(() -> new MuchException(MESSAGE_FOR_USER, "전화번호에 해당하는 사용자를 찾을 수 없습니다."));

        // 기존 휴대폰번호 사용자의 최초 소셜 연결을 확인하여 다른 소셜도 연결

        return null;
    }


    public Optional<User> findUserByPhoneNumber(String phoneNumber) {

        return userRepository.findByPhoneNumber(phoneNumber);
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

}
