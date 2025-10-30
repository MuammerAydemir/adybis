package com.muammer.adybis.user.services.Impls;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.muammer.adybis.base.auth.dtos.RegisterRequest;
import com.muammer.adybis.user.models.User;
import com.muammer.adybis.user.services.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserService USER_SERVICE;

    public User register(RegisterRequest registerRequest) throws Exception {
        if (USER_SERVICE.existUsername(registerRequest.getUsername())) {
            throw new Exception("Username already exist!");
        }
        return USER_SERVICE.save(registerRequest);
    }

    @Override
    public UserDetailsImpl loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = USER_SERVICE.findUserByUsername(username);
        return new UserDetailsImpl(user);

    }

}
