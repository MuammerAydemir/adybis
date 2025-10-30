package com.muammer.adybis.user.services.Impls;

import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.muammer.adybis.user.models.User;

public class UserDetailsImpl implements UserDetails {
    private final User user;

    public UserDetailsImpl(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return user.getPasswordExpirationDate().isAfter(LocalDate.now());
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }

    public boolean isVerified() {
        return user.isVerified();
    }

    public boolean is2FAEnabled() {
        return user.isTwoFAEnabled();
    }

    public UUID getUserId() {
        return user.getId();
    }

}
