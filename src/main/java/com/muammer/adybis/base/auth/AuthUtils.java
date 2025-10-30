package com.muammer.adybis.base.auth;

import java.util.List;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.muammer.adybis.user.services.Impls.UserDetailsImpl;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class AuthUtils {
    public static Authentication currentAuth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static List<String> getCurrentUserRoles() {
        return currentAuth().getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
    }

    public static UUID getCurrentUserId() {
        return ((UserDetailsImpl) currentAuth().getPrincipal()).getUserId();
    }

    public static boolean getCurrentUser2FAEnableValue() {
        return ((UserDetailsImpl) currentAuth().getPrincipal()).is2FAEnabled();
    }
}
