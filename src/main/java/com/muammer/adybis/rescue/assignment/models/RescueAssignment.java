package com.muammer.adybis.rescue.assignment.models;

import java.time.LocalDateTime;
import java.util.UUID;

import com.muammer.adybis.common.enums.StatusType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "rescue_assignments")
public class RescueAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column(name = "help_request_id")
    @NotNull(message = "Help request id cannot be null!")
    private UUID helpRequestId;

    @Column(name = "rescue_team_id")
    @NotNull(message = "Rescue team id cannot be null!")
    private UUID rescueTeamId;

    @Column(name = "created_by")
    @NotNull(message = "Creator id cannot be null!")
    private UUID createdBy;

    @Builder.Default
    @Column(name = "assigned_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime assignedAt = LocalDateTime.now();

    @Builder.Default
    @Column(name = "status", nullable = false)
    private String status = StatusType.AS.label;

    @Column(name = "completed_at")
    private LocalDateTime complatedAt;
}
