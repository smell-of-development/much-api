package much.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import much.api.controller.swagger.UserApi;
import much.api.dto.Jwt;
import much.api.dto.request.JoinInformation;
import much.api.dto.request.SocialUserLinking;
import much.api.dto.response.Envelope;
import much.api.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController implements UserApi {

    private final UserService userService;

    @Override
    @PostMapping
    public ResponseEntity<Envelope<Jwt>> registerUser(@RequestBody @Valid JoinInformation request) {

        return ResponseEntity.ok(userService.registerUser(request));
    }


    @Override
//    @PostMapping("/social-linking")
    public ResponseEntity<Envelope<Jwt>> linkSocialUser(SocialUserLinking request) {

        return ResponseEntity.ok(userService.linkUser(request.getTargetPhoneNumber(), request.getToBeDeletedId()));
    }

}
