package com.muammer.adybis.base.utils;

import java.math.BigDecimal;

public class CalculatingMethods {
    public static double calculateHaversine(BigDecimal lat1, BigDecimal lon1,
            BigDecimal lat2, BigDecimal lon2) {

        final double R = 6371; // Earth radius(km)

        double dLat = Math.toRadians(lat2.doubleValue() - lat1.doubleValue());
        double dLon = Math.toRadians(lon2.doubleValue() - lon1.doubleValue());

        double dLat1 = Math.toRadians(lat1.doubleValue());
        double dLat2 = Math.toRadians(lat2.doubleValue());

        double a = haversine(dLat)
                + Math.cos(Math.toRadians(dLat1)) * Math.cos(Math.toRadians(dLat2))
                        * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

    private static double haversine(double val) {
        return Math.pow(Math.sin(val / 2), 2);
    }
}
