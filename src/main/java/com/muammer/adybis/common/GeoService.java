package com.muammer.adybis.common;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import org.springframework.stereotype.Service;

@Service
public class GeoService {
    public <T> T findNearest(
            BigDecimal requestLat,
            BigDecimal requestLon,
            List<T> targets,
            Function<T, BigDecimal> targetLat,
            Function<T, BigDecimal> targetLon,
            Supplier<? extends RuntimeException> exception) {
        if (targets == null || targets.isEmpty()) {
            throw exception.get();
        }

        T nearest = null;
        double minDistance = Double.MAX_VALUE;

        for (T target : targets) {
            final double R = 6371; // Earth radius(km)

            double dLat1 = Math.toRadians(requestLat.doubleValue());
            double dLon1 = Math.toRadians(requestLon.doubleValue());
            double dLat2 = Math.toRadians(targetLat.apply(target).doubleValue());
            double dLon2 = Math.toRadians(targetLon.apply(target).doubleValue());

            double dLat = Math.toRadians(dLat2 - dLat1);
            double dLon = Math.toRadians(dLon2 - dLon1);

            double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                    + Math.cos(Math.toRadians(dLat1)) * Math.cos(Math.toRadians(dLat2))
                            * Math.sin(dLon / 2) * Math.sin(dLon / 2);

            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            double distance = R * c;

            if (distance < minDistance) {
                minDistance = distance;
                nearest = target;
            }
        }

        if (nearest == null) {
            throw exception.get();
        }

        return nearest;
    }
}