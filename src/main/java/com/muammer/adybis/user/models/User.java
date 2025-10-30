package com.muammer.adybis.user.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.muammer.adybis.role.models.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column(name = "username", unique = true, nullable = false)
    @Pattern(regexp = "[a-zA-Z][a-zA-Z0-9.-_ ]{1,120}", message = "Username is invalid!")
    private String username;

    @Column(name = "email", unique = true, nullable = false, length = 255)
    @Email(message = "Email is invalid!")
    private String email;

    @Column(name = "phone", unique = true, nullable = false, length = 20)
    @Pattern(regexp = "^([+]?\\d{1,2}[-\\s]?|)\\d{3}[-\\s]?\\d{3}[-\\s]?\\d{4}$", message = "Phone number is invalid!")
    private String phone;

    @Column(name = "hashed_password", nullable = false, length = 255)
    @NotNull(message = "Password cannot be null or empty!")
    private String password;

    @Column(name = "birthday_date", nullable = false)
    @NotNull(message = "Doğum tarihi boş olamaz.")
    private LocalDate birthdayDate;

    @Column(name = "blood_type", nullable = false, length = 20)
    @NotNull(message = "Blood type cannot be null or empty!")
    private String bloodType;

    @Builder.Default
    @Column(name = "chronic_illnesses")
    private boolean chronicIllnesses = false;

    @Builder.Default
    @Column(name = "people_with_disabilities")
    private boolean peopleWithDisabilities = false;

    @Column(name = "special_case_description", columnDefinition = "TEXT")
    private String specialCaseDescription;

    @Builder.Default
    @Column(name = "is_2fa_enabled")
    private boolean twoFAEnabled = false;

    @Builder.Default
    @Column(name = "is_verified")
    private boolean verified = false;

    @Builder.Default
    @Column(name = "password_expiration_date", nullable = false)
    private LocalDate passwordExpirationDate = LocalDate.now().plusMonths(3);

    @Builder.Default
    @Column(name = "is_enable", nullable = false)
    private boolean enabled = true;

    @Builder.Default
    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @PrePersist
    public void prePersist() {
        if (this.passwordExpirationDate == null) {
            this.passwordExpirationDate = LocalDate.now().plusMonths(3);
        }
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles;

}
