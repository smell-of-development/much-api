package much.api.controller;

import lombok.RequiredArgsConstructor;
import much.api.controller.swagger.UserApi;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController implements UserApi {


}
