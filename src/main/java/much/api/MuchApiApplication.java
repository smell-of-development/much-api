package much.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class MuchApiApplication {

    /* TODO
    컨트롤러와 서비스계층 사이 DTO 객체 생각해보기
    서비스계층 리턴시 Envelope 감싸지말도록 리팩토링하기
    QueryService, CommandService 분리
    전화번호인증구현
     */
    public static void main(String[] args) {
        SpringApplication.run(MuchApiApplication.class, args);
    }

}
