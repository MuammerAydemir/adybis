package com.muammer.adybis.base.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.muammer.adybis.base.security.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {
    private final JwtAuthenticationFilter JWT_AUTHENTICATION_FILTER;
    private final AppProperties APP_PROPERTIES;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(CsrfConfigurer::disable)
                .authorizeHttpRequests(
                        request -> request.requestMatchers(APP_PROPERTIES.getBaseUrl() + "/auth/**",
                                "/public/**",
                                "/swagger-ui/**",
                                "/api-docs/**").permitAll()
                                .requestMatchers(APP_PROPERTIES.getBaseUrl() + "/users/user/**")
                                .hasAnyAuthority("viewer", "dispatcher", "victim", "rescue_team", "admin")
                                .requestMatchers(APP_PROPERTIES.getBaseUrl() + "/help-requests/**")
                                .hasAnyAuthority("viewer", "dispatcher", "victim", "rescue_team", "admin")
                                .requestMatchers(APP_PROPERTIES.getBaseUrl() + "/help-points/**", "admin")
                                .hasAnyAuthority("dispatcher", "rescue_team", "victim", "viewer", "admin")
                                .requestMatchers(APP_PROPERTIES.getBaseUrl() + "/rescue-assignments/**")
                                .hasAnyAuthority("dispatcher", "rescue_team", "admin")
                                .anyRequest().authenticated())
                .addFilterBefore(JWT_AUTHENTICATION_FILTER, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
