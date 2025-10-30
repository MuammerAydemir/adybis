package com.muammer.adybis.base.seeder;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;
import com.muammer.adybis.common.enums.StatusType;
import com.muammer.adybis.help.request.models.HelpRequest;

@Component
public class HelpRequestFactory {
    private static final Faker faker = new Faker();

    public static HelpRequest.HelpRequestBuilder helpRequestBuilder(UUID id) {
        return HelpRequest.builder().victimId(id).latitude(new BigDecimal(faker.address().latitude().replace(",", ".")))
                .longitude(new BigDecimal(faker.address().longitude().replace(",", ".")))
                .status(faker.options().option(StatusType.class).label).description(faker.lorem().paragraph(5));
    }

    public static HelpRequest createRandomHelpRequest(UUID id) {
        return helpRequestBuilder(id).build();
    }

    public static HelpRequest createRandomExpiredHelpRequest(UUID id) {
        return helpRequestBuilder(id).status(StatusType.CP.label).build();
    }
}
