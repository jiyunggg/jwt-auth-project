package com.baro.jwt_auth.config;

import com.baro.jwt_auth.common.exception.ErrorCode;
import com.baro.jwt_auth.common.exception.dto.ResErrorDTO;
import com.baro.jwt_auth.security.jwt.JwtAuthFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/signup", "/api/login").permitAll()
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/v3/api-docs.yaml",
                                "/swagger-resources/**",
                                "/webjars/**"
                        ).permitAll()
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            ErrorCode errorCode = ErrorCode.INVALID_TOKEN;

                            response.setStatus(errorCode.getStatus().value());
                            response.setContentType("application/json; charset=UTF-8");

                            ResErrorDTO errorBody = ResErrorDTO.of(errorCode.getCode(), errorCode.getMessage());
                            String json = new ObjectMapper().writeValueAsString(errorBody);

                            response.getWriter().write(json);
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            ErrorCode errorCode = ErrorCode.ACCESS_DENIED;

                            response.setStatus(errorCode.getStatus().value());
                            response.setContentType("application/json; charset=UTF-8");

                            ResErrorDTO errorBody = ResErrorDTO.of(errorCode.getCode(), errorCode.getMessage());
                            String json = new ObjectMapper().writeValueAsString(errorBody);

                            response.getWriter().write(json);
                        })
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
