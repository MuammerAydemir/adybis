package com.muammer.adybis.base.utils;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

import static org.assertj.core.api.Assertions.assertThat;
import java.math.BigDecimal;

@Slf4j
public class CalculatingMethodsTest {

    @Test
    void testCalculateHaversine() {
        BigDecimal originLat = BigDecimal.valueOf(37.975598829375286);
        BigDecimal originLong = BigDecimal.valueOf(32.49468303774186);
        BigDecimal targetLat = BigDecimal.valueOf(37.97520364757795);
        BigDecimal targetLong = BigDecimal.valueOf(32.51460553241891);

        double result = CalculatingMethods.calculateHaversine(originLat, originLong, targetLat, targetLong);

        assertThat(result).as("The calculated distance value is incorrect!").isEqualTo(2.215567918019904);
        log.info("✅ Haversine calculate test completed!");
    }
}
