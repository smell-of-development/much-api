package much.api;

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
    }

}
