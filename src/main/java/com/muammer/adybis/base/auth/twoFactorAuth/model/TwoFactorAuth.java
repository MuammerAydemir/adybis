package com.muammer.adybis.base.auth.twoFactorAuth.model;

import java.time.LocalDateTime;
import java.util.UUID;

import com.muammer.adybis.base.auth.twoFactorAuth.enums.TwoFactorMethod;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "two_factor_auth")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TwoFactorAuth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column(name = "method", nullable = false, length = 10)
    @Builder.Default
    private String method = TwoFactorMethod.EMAIL.toString();

    @Column(name = "code", nullable = false, length = 10)
    @NotBlank(message = "Code cannot be null or empty!")
    private String code;

    @Column(name = "expires_at", nullable = false)
    @NotNull(message = "Expires time cannot be null or empty!")
    private LocalDateTime expiresAt;

    @Column(name = "is_used", nullable = false)
    @Builder.Default
    private boolean used = false;

    @Builder.Default
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "user_id", nullable = false)
    @NotNull(message = "User id cannot be null or empty!")
    private UUID userId;
}
