package much.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import much.api.controller.swagger.UserApiV1;
import much.api.dto.request.UserCreation;
import much.api.dto.request.SocialUserLinking;
import much.api.dto.response.Envelope;
import much.api.dto.response.WebToken;
import much.api.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserControllerV1 implements UserApiV1 {

    private final UserService userService;

    @Override
    @PostMapping("/user")
    public ResponseEntity<Envelope<WebToken>> createUser(@RequestBody @Valid UserCreation request) {

        return ok(
                Envelope.ok(userService.registerUser(request))
        );
    }


    @Override
//    @PostMapping("/social-linking")
    public ResponseEntity<Envelope<WebToken>> linkSocialUser(SocialUserLinking request) {

        return ok(
                Envelope.ok(userService.linkUser(request.getTargetPhoneNumber(), request.getToBeDeletedId()))
        );
    }

}
