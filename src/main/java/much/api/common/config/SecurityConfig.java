package much.api.common.config;

import lombok.RequiredArgsConstructor;
import much.api.common.exception.JwtAccessDeniedHandler;
import much.api.common.exception.JwtAuthenticationEntryPoint;
import much.api.common.filter.JwtFilter;
import much.api.common.util.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private static final String[] SWAGGER_URL_ARRAY = {
            "/swagger-ui/**",
            "/api-docs/**"
    };

    private final TokenProvider tokenProvider;

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

                .authorizeHttpRequests(registry -> registry
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
                        .requestMatchers(SWAGGER_URL_ARRAY).permitAll()
                        .requestMatchers("/common/**").permitAll()
                        .requestMatchers("/oauth2/**").permitAll()
                        .requestMatchers("/auth/refresh").permitAll()
                        .anyRequest().authenticated())

                .exceptionHandling(configurer -> configurer
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

                .sessionManagement(configurer -> configurer
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .getOrBuild();
    }

}