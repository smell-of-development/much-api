package much.api.service;

import lombok.RequiredArgsConstructor;
import much.api.common.aop.MuchValid;
import much.api.common.enums.Role;
import much.api.common.exception.VerificationNeeded;
import much.api.common.exception.InvalidPhoneNumber;
import much.api.common.util.PhoneNumberUtils;
import much.api.common.util.TokenProvider;
import much.api.dto.request.UserCreation;
import much.api.dto.response.WebToken;
import much.api.entity.SmsVerificationHist;
import much.api.entity.User;
import much.api.repository.SmsVerificationHistRepository;
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

    private final CommonService commonService;

    private final UserRepository userRepository;

    private final SmsVerificationHistRepository smsVerificationHistRepository;

    private final PasswordEncoder passwordEncoder;

    private final TokenProvider tokenProvider;


    @Transactional
    public WebToken createUser(@MuchValid UserCreation userCreation) {

        // 로그인 ID 중복체크
        commonService.checkDuplicatedLoginId(userCreation.getLoginId());
        // 닉네임 중복체크
        commonService.checkDuplicatedNickname(userCreation.getNickname());
        // 이미 가입된 휴대폰번호인지 검사
        final String phoneNumber = userCreation.getPhoneNumber();
        commonService.checkDuplicatedPhoneNumber(phoneNumber);

        // 휴대폰인증 SELECT - 1시간 이내
        LocalDateTime after = LocalDateTime.now().minusHours(1L);
        Optional<SmsVerificationHist> histOptional = smsVerificationHistRepository.findLatestSent(phoneNumber, after);

        // 개발환경 + DB 개발 파라미터 확인하여 인증 건너뛰기
        boolean verificationCompleted = commonService.isSmsPass();

        // 인증이 완료된 번호인지 확인
        if (!verificationCompleted && histOptional.isPresent()) {

            SmsVerificationHist hist = histOptional.get();
            if (hist.isVerified()) {
                verificationCompleted = true;
            }
        }

        if (!verificationCompleted) {
            throw new VerificationNeeded(phoneNumber);
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
        final User toBeDeletedUser = commonService.getUserOrThrowException(toDeletedId);

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

}
