package much.api.common.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import much.api.common.properties.JwtProperties;
import much.api.common.util.TokenProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProperties properties;

    private final TokenProvider tokenProvider;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        String tokenHeader = request.getHeader(properties.getHeader());

        try {
            if (StringUtils.hasText(tokenHeader) && tokenHeader.startsWith(properties.getType())) {

                String accessToken = tokenHeader.substring(properties.getType().length());
                DecodedJWT decodedJWT = tokenProvider.parse(accessToken);

                Authentication authentication = tokenProvider.getAuthentication(decodedJWT);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("Security Context '{}' 인증 정보를 저장했습니다. uri: {}", authentication.getName(), requestURI);
            } else {
                log.debug("요청에 토큰 없음. uri: {}", requestURI);
            }
        } catch (JWTVerificationException e) {
            log.debug("유효한 JWT 토큰이 아님. uri: {}", requestURI, e);
        }

        filterChain.doFilter(request, response);
    }

}
