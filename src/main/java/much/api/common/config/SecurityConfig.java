package much.api.common.config;

import lombok.RequiredArgsConstructor;
import much.api.common.JwtAccessDeniedHandler;
import much.api.common.JwtAuthenticationEntryPoint;
import much.api.filter.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toStaticResources;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private static final String[] SWAGGER_URL_ARRAY = {
            "/swagger-ui/**",
            "/api-docs/**"
    };

    private final JwtFilter jwtFilter;

    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .headers(configurer -> configurer
                        .frameOptions()
                        .sameOrigin()
                        .disable())

                .cors().and()
                .csrf().disable()
                .formLogin().disable()
                .logout().disable()
                .httpBasic().disable()

                // 메소드 시큐리티 사용
                .authorizeHttpRequests(registry -> registry
                                .requestMatchers(SWAGGER_URL_ARRAY).permitAll()
                                .requestMatchers(antMatcher("/h2-console/**")).permitAll()
                                .requestMatchers(toStaticResources().atCommonLocations()).permitAll()
                                .requestMatchers("/api/v1/common/**").permitAll()
//                        .requestMatchers("/oauth2/**").permitAll()
                                .requestMatchers("/api/v1/auth/refresh").permitAll()
                                .requestMatchers("/api/v1/sms/**").permitAll()
                                .requestMatchers("/api/v1/login").permitAll()
                                .requestMatchers("/api/v1/testToken").permitAll()
                                .requestMatchers(POST, "/api/v1/user").permitAll()
                                .requestMatchers(GET, "/api/v1/community/**").permitAll()
                                .anyRequest().authenticated()
                )
                .exceptionHandling(configurer -> configurer
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

                .sessionManagement(configurer -> configurer
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .getOrBuild();
    }


    @Bean
    PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.setAllowedOriginPatterns(List.of("http://localhost:3000"));
        corsConfiguration.setAllowedMethods(List.of("HEAD", "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

}
