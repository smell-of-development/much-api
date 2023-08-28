package much.api;

import lombok.RequiredArgsConstructor;
import much.api.common.enums.Role;
import much.api.entity.User;
import much.api.repository.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Collections;
import java.util.Set;

@RequiredArgsConstructor
public class WithMockUserSecurityContext implements WithSecurityContextFactory<WithUser> {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public SecurityContext createSecurityContext(WithUser annotation) {

        final String loginId = annotation.loginId();
        final String password = annotation.password();
        final String nickname = annotation.nickname();
        final Role role = Role.valueOf(annotation.role());

        User user = User.builder()
                .loginId(loginId)
                .password(passwordEncoder.encode(password))
                .nickname(nickname)
                .role(role)
                .build();

        userRepository.save(user);

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.name());
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(authority);

        org.springframework.security.core.userdetails.User userDetails =
                new org.springframework.security.core.userdetails.User(String.valueOf(user.getId()), "", authorities);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "", authorities);

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);

        return securityContext;
    }

}
