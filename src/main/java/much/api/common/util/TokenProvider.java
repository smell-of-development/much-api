package much.api.common.util;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import much.api.common.enums.Role;
import much.api.common.properties.JwtProperties;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {

    private final JwtProperties properties;


    public String createAccessToken(String id, Role role) {

        return Jwts.builder()
                .setSubject(id)
                .claim("role", role)
                .setExpiration(new Date(System.currentTimeMillis() + properties.getAccessTokenExpirationTime()))
                .signWith(SignatureAlgorithm.HS512, properties.getSecret())
                .compact();
    }


    public String createRefreshToken(String id) {

        return Jwts.builder()
                .setSubject(id)
                .setExpiration(new Date(System.currentTimeMillis() + properties.getRefreshTokenExpirationTime()))
                .signWith(SignatureAlgorithm.HS512, properties.getSecret())
                .compact();
    }


    public boolean isAccessToken(String token) {

        return Jwts.parser()
                .setSigningKey(properties.getSecret())
                .parseClaimsJws(token)
                .getBody()
                .get("role") != null;
    }


    public boolean isExpiredToken(String token) {

        try {
            parseClaims(token);
            return false;
        } catch (ExpiredJwtException e) {
            return true;
        }
    }


    public boolean isValidToken(String token) {

        try {
            parseClaims(token);
            return true;
        } catch (SignatureException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰값이 없습니다.");
        }

        return false;
    }

    private void parseClaims(String token) {
        Jwts.parser().setSigningKey(properties.getSecret()).parseClaimsJws(token);
    }


    public Authentication getAuthentication(String token) {

        Claims claims = Jwts.parser()
                .setSigningKey(properties.getSecret())
                .parseClaimsJws(token)
                .getBody();

        List<SimpleGrantedAuthority> authorities = Arrays.stream(claims.get("role").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .toList();

        User user = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(user, token, authorities);
    }

}
