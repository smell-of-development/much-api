package much.api.controller;

import lombok.RequiredArgsConstructor;
import much.api.common.util.TokenProvider;
import much.api.controller.swagger.UserApi;
import much.api.dto.request.PositionSetting;
import much.api.dto.response.Envelope;
import much.api.dto.response.OAuth2;
import much.api.entity.User;
import much.api.exception.InvalidValueException;
import much.api.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static much.api.dto.response.Envelope.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController implements UserApi {

    private final UserService userService;

    private final TokenProvider tokenProvider;

    @Override
    @PostMapping("/init")
    public ResponseEntity<Envelope<OAuth2>> initUser(@RequestBody PositionSetting request) {

        final Long id = request.getId();
        final String positionIds = request.getPositionIds();
        final String positionClass = request.getPositionClass();

        if (id == null) {
            throw new InvalidValueException("id");
        }
        if (!StringUtils.hasText(positionIds)) {
            throw new InvalidValueException("positionIds");
        }
        if (!StringUtils.hasText(positionClass)) {
            throw new InvalidValueException("positionClass");
        }

        User user = userService.initUser(id, positionIds, positionClass);
        String idToString = id.toString();

        String accessToken = tokenProvider.createAccessToken(idToString, user.getRole());
        String refreshToken = tokenProvider.createRefreshToken(idToString);

        return ResponseEntity.ok(ok(OAuth2.builder()
                .id(id)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build()));
    }

}
