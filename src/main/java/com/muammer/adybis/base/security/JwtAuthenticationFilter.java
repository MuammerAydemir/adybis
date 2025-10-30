package com.muammer.adybis.base.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.muammer.adybis.base.configs.AppProperties;
import com.muammer.adybis.user.services.Impls.UserDetailsImpl;
import com.muammer.adybis.user.services.Impls.UserDetailsServiceImpl;

import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService JWT_SERVICE;
    private final UserDetailsServiceImpl USER_DETAILS_SERVICE_IMPL;
    private final AppProperties APP_PROPERTIES;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        final String AUTH_HEADER = request.getHeader("Authorization");
        final String AUTH_SCHEME = "Bearer ";
        if (AUTH_HEADER == null || !AUTH_HEADER.startsWith(AUTH_SCHEME)) {
            filterChain.doFilter(request, response);
            return;
        }
        final String TOKEN = AUTH_HEADER.substring(AUTH_SCHEME.length());

        String username = JWT_SERVICE.extractUserName(TOKEN);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetailsImpl userDetails = USER_DETAILS_SERVICE_IMPL.loadUserByUsername(username);

            if (JWT_SERVICE.isTokenValid(TOKEN, userDetails)) {
                Authentication authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request, response);

    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();

        return path.equals("/public/") || path.equals(APP_PROPERTIES.getBaseUrl() + "/auth/login")
                || path.equals(APP_PROPERTIES.getBaseUrl() + "/auth/register")
                || path.equals("/swagger-ui/index.html/")
                || path.equals("/api-docs/");
    }

}
