package com.muammer.adybis.rescue.team.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.muammer.adybis.rescue.enums.RescueTeamStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "rescue_team_locations")
public class RescueTeamLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column(name = "rescue_team_id", nullable = false)
    @NotNull(message = "Rescue Team id  cannot be null or empty!")
    private UUID rescueTeamId;

    @Column(name = "latitude", nullable = false, precision = 10, scale = 8)
    @NotNull(message = "latitude cannot be null or empty!")
    private BigDecimal latitude;

    @Column(name = "longitude", nullable = false, precision = 11, scale = 8)
    @NotNull(message = "longitude cannot be null or empty!")
    private BigDecimal longitude;

    @Builder.Default
    @Column(name = "status", nullable = false)
    private String status = RescueTeamStatus.AC.toString();

    @Builder.Default
    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
