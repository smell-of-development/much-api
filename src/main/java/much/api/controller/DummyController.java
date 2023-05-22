package much.api.controller;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
public class DummyController {

    @GetMapping("/favicon.ico")
    public void handleFavicon() {

    }

}
