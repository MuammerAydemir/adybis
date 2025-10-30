package com.muammer.adybis.base.auth.twoFactorAuth.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.muammer.adybis.base.auth.twoFactorAuth.model.TwoFactorAuth;

import java.util.Optional;

public interface TwoFactorAuthRepository extends JpaRepository<TwoFactorAuth, UUID> {
    Optional<TwoFactorAuth> findByUserIdAndCodeAndUsedFalse(UUID userId, String code);
}
