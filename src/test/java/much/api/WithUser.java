package much.api;

import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockUserSecurityContext.class, setupBefore = TestExecutionEvent.TEST_EXECUTION)
public @interface WithUser {

    String loginId();

    String password();

    String nickname();

    String role() default "ROLE_USER";

}
