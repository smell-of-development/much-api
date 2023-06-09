package much.api.common.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.*;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import much.api.common.enums.Role;
import much.api.common.properties.JwtProperties;
import much.api.dto.Jwt;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {

    public static final String CLAIM_ROLE = "role";

    private final JwtProperties properties;


    public Jwt createTokenResponse(String id, Role role) {

        String accessToken = this.createAccessToken(id, role);
        String refreshToken = this.createRefreshToken(id);

        return Jwt.builder()
                .id(Long.parseLong(id))
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }


    public String createAccessToken(String id, Role role) {

        return JWT.create()
                .withSubject(id)
                .withClaim(CLAIM_ROLE, role.name())
                .withExpiresAt(new Date(System.currentTimeMillis() + properties.getAccessTokenExpirationTime()))
                .sign(Algorithm.HMAC512(properties.getSecret()));
    }


    public String createRefreshToken(String id) {

        return JWT.create()
                .withSubject(id)
                .withExpiresAt(new Date(System.currentTimeMillis() + properties.getRefreshTokenExpirationTime()))
                .sign(Algorithm.HMAC512(properties.getSecret()));
    }


    public boolean isExpiredToken(String token) {

        try {
            parse(token);
            return false;
        } catch (TokenExpiredException e) {
            log.info("토큰기한 만료[{}]", token);
            return true;
        } catch (Exception e) {
            log.info("토큰해독 불가[{}]", token);
            return false;
        }
    }


    public boolean isValidToken(String token) {

        try {
            parse(token);
            return true;
        } catch (Exception e) {
            log.info("토큰해독 불가[{}]", token);
            return false;
        }
    }


    public boolean isValidAccessToken(String token) {

        try {
            DecodedJWT decodedJWT = parse(token);

            return !decodedJWT.getClaim(CLAIM_ROLE)
                    .isMissing();
        } catch (Exception e) {
            log.info("토큰해독 불가[{}]", token);
            return false;
        }
    }


    public boolean isValidRefreshToken(String token) {

        return isValidToken(token) && extractClaim(token, CLAIM_ROLE) == null;
    }


    public String extractSubject(String token) {

        try {
            return JWT.decode(token).getSubject();
        } catch (JWTDecodeException e) {
            throw new InsufficientAuthenticationException("토큰 디코딩 실패");
        }
    }


    public String extractClaim(String token, String claim) {

        try {
            return JWT.decode(token).getClaim(claim).asString();
        } catch (JWTDecodeException e) {
            throw new InsufficientAuthenticationException("토큰 디코딩 실패");
        }
    }


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

}
