package much.api;

import much.api.common.enums.Role;
import much.api.common.util.TokenProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class MuchApiApplicationTests {

    @Autowired
    TokenProvider tokenProvider;

    @Test
    void contextLoads() {
        String accessToken = tokenProvider.createAccessToken("1", Role.ROLE_USER);
        String refreshToken = tokenProvider.createRefreshToken("1");
        String refreshToken2 = tokenProvider.createRefreshToken("2");

        System.out.println("accessToken = " + accessToken);
        System.out.println("refreshToken = " + refreshToken);
        System.out.println("refreshToken2 = " + refreshToken2);

    }

}
