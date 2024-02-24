package much.api.common.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import much.api.common.enums.Role;
import much.api.common.properties.JwtProperties;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

import static java.lang.String.format;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {

    public static final String CLAIM_ROLE = "role";

    private static final String CLAIM_PAIR_KEY = "pair_key";

    private final JwtProperties properties;


    /**
     * 토큰을 해독합니다.
     *
     * @param token 토큰
     * @return 해독 된 JWT
     * @throws TokenExpiredException    토큰 만료
     * @throws JWTVerificationException 해독 실패
     */
    public DecodedJWT parse(String token) throws TokenExpiredException, JWTVerificationException {

        return JWT.require(Algorithm.HMAC512(properties.getSecret()))
                .build()
                .verify(token);
    }


    public Authentication getAuthentication(DecodedJWT decodedJWT) {

        String accessToken = decodedJWT.getToken();

        String role = decodedJWT.getClaim(CLAIM_ROLE).asString();
        if (role == null) throw new JWTVerificationException("액세스 토큰이 아님");

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(authority);

        User user = new User(decodedJWT.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(user, accessToken, authorities);
    }


    public Long getSubject(String token) {

        return extractSubject(token);
    }


    public Jwt createTokenResponse(Long id, Role role) {

        String uuid = UUID.randomUUID().toString();

        String accessToken = this.createAccessToken(id, role, uuid);
        String refreshToken = this.createRefreshToken(id, uuid);

        return Jwt.builder()
                .id(id)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }


    public Jwt checkRefreshableAndCreateToken(String accessToken, String refreshToken, Role role) {

        checkValidAccessToken(accessToken, true);
        checkValidRefreshToken(refreshToken);

        // 액세스 토큰의 유저 ID 추출
        Long userIdAtAccessToken = extractSubject(accessToken);
        // 리프레시 토큰의 유저 ID 추출
        Long userIdAtRefreshToken = extractSubject(refreshToken);

        // 액세스 토큰의 UUID 추출
        String uuidAtAccessToken = extractClaim(accessToken, CLAIM_PAIR_KEY);
        // 리프레시 토큰의 UUID 추출
        String uuidAtRefreshToken = extractClaim(refreshToken, CLAIM_PAIR_KEY);

        // 같은 유저에 대한 토큰이 아니거나, 짝이 안맞으면 리프레시 불가
        if (!userIdAtAccessToken.equals(userIdAtRefreshToken) || !uuidAtAccessToken.equals(uuidAtRefreshToken)) {
            throw new InsufficientAuthenticationException("토큰 리프레시 불가");
        }

        return createTokenResponse(userIdAtAccessToken, role);
    }


    private String createAccessToken(Long id, Role role, String uuid) {

        return getJwtCommonBuilder(id, uuid, properties.getAccessTokenExpirationTime())
                .withClaim(CLAIM_ROLE, role.name())
                .sign(Algorithm.HMAC512(properties.getSecret()));
    }


    private String createRefreshToken(Long id, String uuid) {

        return getJwtCommonBuilder(id, uuid, properties.getRefreshTokenExpirationTime())
                .sign(Algorithm.HMAC512(properties.getSecret()));
    }


    private void checkValidAccessToken(String token, boolean ignoreExpired) {

        try {
            parse(token);
        } catch (TokenExpiredException e) {
            if (!ignoreExpired) {
                throw new InsufficientAuthenticationException("토큰검증 실패 - 유효기간 만료", e);
            }
        } catch (Exception e) {
            throw new InsufficientAuthenticationException("토큰검증 실패", e);
        }

        String roleClaim = extractClaim(token, CLAIM_ROLE);
        if (roleClaim == null) {
            throw new InsufficientAuthenticationException("액세스 토큰이 아님");
        }
    }


    private void checkValidRefreshToken(String token) {

        DecodedJWT decodedJWT;
        try {
            decodedJWT = parse(token);
        } catch (Exception e) {
            throw new InsufficientAuthenticationException("토큰검증 실패", e);
        }

        if (decodedJWT == null || !decodedJWT.getClaim(CLAIM_ROLE).isMissing()) {
            throw new InsufficientAuthenticationException("리프레시 토큰이 아님");
        }
    }


    private Long extractSubject(String token) {

        try {
            return Long.valueOf(JWT.decode(token).getSubject());
        } catch (JWTDecodeException e) {
            throw new InsufficientAuthenticationException(format("토큰[%s] - decode() 실패", token));
        }
    }


    private String extractClaim(String token, String claim) {

        try {
            return JWT.decode(token).getClaim(claim).asString();
        } catch (JWTDecodeException e) {
            throw new InsufficientAuthenticationException(format("토큰[%s] - getClaim(%s) 실패", token, claim));
        }
    }


    private JWTCreator.Builder getJwtCommonBuilder(Long id, String uuid, long expirationTimes) {

        return JWT.create()
                .withSubject(String.valueOf(id))
                .withClaim(CLAIM_PAIR_KEY, uuid)
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTimes));
    }


    @Getter
    @Builder
    public static class Jwt {

        private Long id;

        private String accessToken;

        private String refreshToken;

    }

}
