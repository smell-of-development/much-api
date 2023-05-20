package much.api.common.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import much.api.common.properties.JwtProperties;
import much.api.common.util.TokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

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

        Optional<String> resolvedToken = resolveToken(request);
        if (resolvedToken.isEmpty()) {
            log.debug("유효한 JWT 토큰이 없습니다. uri: {}", requestURI);
        } else {
            String token = resolvedToken.get();
            Authentication authentication = tokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug("Security Context '{}' 인증 정보를 저장했습니다. uri: {}", authentication.getName(), requestURI);
        }

        filterChain.doFilter(request, response);
    }


    private Optional<String> resolveToken(HttpServletRequest request) {

        String tokenHeader = request.getHeader(properties.getHeader());

        if (StringUtils.hasText(tokenHeader) && tokenHeader.startsWith(properties.getType())) {

            String accessToken = tokenHeader.substring(properties.getType().length());

            return tokenProvider.isValidToken(accessToken) ? Optional.of(accessToken) : Optional.empty();
        }

        return Optional.empty();
    }

}
