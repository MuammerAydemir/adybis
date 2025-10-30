package com.muammer.adybis.base.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.muammer.adybis.user.services.Impls.UserDetailsImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthUtilsTest {

    private final UUID USER_ID = UUID.randomUUID();
    private UserDetailsImpl mockUser;
    private Authentication mockAuth;

    @BeforeEach
    void setup() {
        mockUser = mock(UserDetailsImpl.class);
        mockAuth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(mockAuth);
        SecurityContextHolder.setContext(securityContext);

        when(mockAuth.getPrincipal()).thenReturn(mockUser);

        when(mockUser.getUserId()).thenReturn(USER_ID);
        when(mockUser.is2FAEnabled()).thenReturn(true);
        when(mockAuth.getAuthorities())
                .thenReturn((Collection) List.of(new SimpleGrantedAuthority("admin"),
                        new SimpleGrantedAuthority("dispatcher")));

    }

    @Test
    void testGetCurrentUserId() {
        UUID result = AuthUtils.getCurrentUserId();
        assertThat(result).isEqualByComparingTo(USER_ID);
        log.info("✅ Test to retrieve the user's ID successfully completed!");
    }

    @Test
    void testGetCurrentUserRoles() {
        List<String> roles = AuthUtils.getCurrentUserRoles();
        assertThat(roles.contains("admin")).isTrue();
        assertThat(roles.contains("dispatcher")).isTrue();
        assertThat(roles.size()).isEqualTo(2);
        log.info("✅ Test to retrieve the user's roles successfully completed!");

    }

    @Test
    void testGetCurrentUser2FAValue() {
        boolean result = AuthUtils.getCurrentUser2FAEnableValue();
        assertThat(result).isTrue();
        log.info("✅ Test to retrieve the user's 2FA auth successfully completed!");

    }
}
