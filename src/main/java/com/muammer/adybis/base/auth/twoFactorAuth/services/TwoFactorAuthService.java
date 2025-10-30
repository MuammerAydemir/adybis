package com.muammer.adybis.base.auth.twoFactorAuth.services;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.muammer.adybis.base.auth.twoFactorAuth.enums.TwoFactorMethod;
import com.muammer.adybis.base.auth.twoFactorAuth.model.TwoFactorAuth;
import com.muammer.adybis.base.auth.twoFactorAuth.repository.TwoFactorAuthRepository;
import com.muammer.adybis.user.models.User;
import com.muammer.adybis.user.services.UserService;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TwoFactorAuthService {
    private final EmailService EMAIL_SERVICE;
    private final TwoFactorAuthRepository TWO_FACTOR_AUTH_REPOSITORY;
    private final UserService USER_SERVICE;

    public TwoFactorAuth generatedCodeAndSend(TwoFactorMethod method, String username)
            throws MessagingException {
        String code = String.format("%06d", new SecureRandom().nextInt(1000000));
        User userInfo = USER_SERVICE.findUserByUsername(username);
        TwoFactorAuth token = TwoFactorAuth.builder()
                .method(method.toString())
                .userId(userInfo.getId())
                .code(code)
                .expiresAt(LocalDateTime.now().plusMinutes(3))
                .used(false)
                .build();
        saveCode(token);
        if (method == TwoFactorMethod.EMAIL) {
            Map<String, Object> model = Map.ofEntries(
                    Map.entry("username", username), Map.entry("code", token.getCode()),
                    Map.entry("appName", "adybis"), Map.entry("year", LocalDate.now().getYear()));
            sendCodeMail(userInfo.getEmail(), model);
        }
        return token;
    }

    public void saveCode(TwoFactorAuth token) {
        TWO_FACTOR_AUTH_REPOSITORY.save(token);
    }

    public void sendCodeMail(String address,
            Map<String, Object> model) throws MessagingException {
        EMAIL_SERVICE.sendCode(address, model);
    }

    public boolean verifyCode(UUID userId, String code) {
        return TWO_FACTOR_AUTH_REPOSITORY.findByUserIdAndCodeAndUsedFalse(userId, code)
                .filter(t -> t.getExpiresAt().isAfter(LocalDateTime.now()))
                .map(t -> {
                    t.setUsed(true);
                    TWO_FACTOR_AUTH_REPOSITORY.save(t);
                    return true;
                })
                .orElse(false);
    }
}
