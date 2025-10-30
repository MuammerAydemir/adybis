package com.muammer.adybis.base.seeder;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;
import com.muammer.adybis.help.enums.HelpPointType;
import com.muammer.adybis.help.point.models.HelpPoint;

@Component
public class HelpPointFactory {
    private static final Faker faker = new Faker();

    public static HelpPoint.HelpPointBuilder helpPointBuilder(UUID id) {

        return HelpPoint.builder()
                .name(faker.address().city() + faker.numerify("###"))
                .createdById(id)
                .latitude(new BigDecimal(faker.address().latitude().replace(",", ".")))
                .longitude(new BigDecimal(faker.address().longitude().replace(",", ".")))
                .type(faker.options().option(HelpPointType.class).label)
                .description(faker.lorem().paragraph(8));
    }

    public static HelpPoint createRandomHelpPoint(UUID id) {
        return helpPointBuilder(id).build();
    }

}
