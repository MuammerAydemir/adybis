package com.muammer.adybis.base.seeder;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;
import com.muammer.adybis.rescue.enums.RescueTeamStatus;
import com.muammer.adybis.rescue.team.models.RescueTeamLocation;

@Component
public class RescueTeamFactory {
    private static final Faker faker = new Faker();

    public static RescueTeamLocation.RescueTeamLocationBuilder rescueTeamBuilder(UUID id) {

        return RescueTeamLocation.builder()
                .rescueTeamId(id)
                .latitude(new BigDecimal(faker.address().latitude().replace(",", ".")))
                .longitude(new BigDecimal(faker.address().longitude().replace(",", ".")))
                .status(RescueTeamStatus.AC.toString());
    }

    public static RescueTeamLocation createRandomRescueTeamLocation(UUID id) {
        return rescueTeamBuilder(id).build();
    }
}
