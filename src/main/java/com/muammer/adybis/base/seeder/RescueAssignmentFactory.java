package com.muammer.adybis.base.seeder;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.muammer.adybis.common.enums.StatusType;
import com.muammer.adybis.rescue.assignment.models.RescueAssignment;

@Component
public class RescueAssignmentFactory {

    private static RescueAssignment.RescueAssignmentBuilder resAssignmentBuilder(UUID helpId, UUID temId,
            UUID dispacId) {
        return RescueAssignment.builder().helpRequestId(helpId).rescueTeamId(temId).createdBy(dispacId)
                .status(StatusType.AS.label);
    }

    public static RescueAssignment createRandomRescueAssignment(UUID helpId, UUID teamId, UUID dispacId) {
        return resAssignmentBuilder(helpId, teamId, dispacId).build();
    }
}
