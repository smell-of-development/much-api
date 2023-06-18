package much.api.service;

import lombok.RequiredArgsConstructor;
import much.api.common.enums.Role;
import much.api.common.util.PhoneNumberUtils;
import much.api.common.util.TokenProvider;
import much.api.common.util.ValidationChecker;
import much.api.dto.Jwt;
import much.api.dto.request.JoinInformation;
import much.api.dto.response.Envelope;
import much.api.entity.Position;
import much.api.entity.User;
import much.api.exception.*;
import much.api.repository.PositionRepository;
import much.api.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static java.lang.String.*;
import static much.api.common.enums.Code.*;
import static much.api.dto.response.Envelope.*;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final CommonService commonService;

    private final PositionRepository positionRepository;

    private final PasswordEncoder passwordEncoder;

    private final TokenProvider tokenProvider;



    @Override
    @Transactional
    public Envelope<Jwt> registerUser(JoinInformation joinInformation) {

        // 로그인 ID 형식, 중복체크
        commonService.checkDuplicatedLoginId(joinInformation.getId());

        // 비밀번호 형식 체크
        if (!ValidationChecker.isValidPassword(joinInformation.getPassword())) {
            throw new BusinessException(INVALID_PASSWORD, "패스워드 형식에 맞지 않음");
        }

        // 닉네임 형식, 중복체크
        commonService.checkDuplicatedNickname(joinInformation.getNickname());

        // 휴대폰번호 형식 검사
        final String phoneNumber = joinInformation.getPhoneNumber();
        if (!PhoneNumberUtils.isOnlyDigitsPattern(phoneNumber)) {
            throw new BusinessException(NOT_MATCHED_PHONE_NUMBER_PATTERN, format("휴대폰번호 형식이 아님. [%s]", phoneNumber));
        }

        // 휴대폰번호 중복 검사
        if (userRepository.findByPhoneNumber(phoneNumber).isPresent()) {
            throw new BusinessException(DUPLICATED_PHONE_NUMBER, format("휴대폰번호 중복. [%s]", phoneNumber));
        }

        // 포지션코드 확인
        final Integer jobGroupCode = joinInformation.getJobGroup();
        final Integer careerCode = joinInformation.getCareer();

        Position jobGroup = positionRepository.findByJogGroupCode(jobGroupCode)
                .orElseThrow(() -> new BusinessException(POSITION_CODE_NOT_FOUNT, "직군 코드와 불일치"));
        Position career = positionRepository.findByCareerCode(careerCode)
                .orElseThrow(() -> new BusinessException(POSITION_CODE_NOT_FOUNT, "경력 코드와 불일치"));

        // 유저 등록
        User user = User.builder()
                .loginId(joinInformation.getId())
                .password(passwordEncoder.encode(joinInformation.getPassword()))
                .nickname(joinInformation.getNickname())
                .phoneNumber(joinInformation.getPhoneNumber())
                .jobGroup(jobGroup)
                .career(career)
                .role(Role.ROLE_USER)
                .refreshable(true)
                .build();

        userRepository.save(user);

        return ok(tokenProvider.createTokenResponse(user.getId().toString(), user.getRole()));
    }

    @Override
    @Transactional
    public Envelope<Jwt> linkUser(String targetPhoneNumber, Long toDeletedId) {

        // 사용자 확인
        final User toBeDeletedUser = userRepository.findById(toDeletedId)
                .orElseThrow(() -> new BusinessException(USER_NOT_FOUND, format("사용자 [%s] 를 찾지 못함", toDeletedId)));

        // 휴대폰 인증이 완료된 다른 멀쩡한 사용자와 연동되는것 방지
        if (toBeDeletedUser.isPhoneVerificationCompleted()) {

        }

        // 휴대폰번호 형식 검사
        if (!PhoneNumberUtils.isOnlyDigitsPattern(targetPhoneNumber)) {
            throw new BusinessException(NOT_MATCHED_PHONE_NUMBER_PATTERN, format("휴대폰번호 형식이 아님. [%s]", targetPhoneNumber));
        }

        // 휴대폰번호 사용자
        final User targetUser = userRepository.findByPhoneNumber(targetPhoneNumber)
                .orElseThrow(() -> new BusinessException(USER_NOT_FOUND_FOR_PHONE_NUMBER, format("[%s]번호에 해당하는 사용자를 찾지 못함", targetPhoneNumber)));

        // 기존 휴대폰번호 사용자의 최초 소셜 연결을 확인하여 다른 소셜도 연결

        return null;
    }

    @Override
    public Optional<User> findUserByPhoneNumber(String phoneNumber) {

        return userRepository.findByPhoneNumber(phoneNumber);
    }

}
