package com.resnik.util.geo;

import com.resnik.util.math.MathUtils;

/**
 * Refs https://www.movable-type.co.uk/scripts/latlong.html
 */
public final class GeoUtils {
    public static final double EARTH_RADIUS_METERS = 6371e3;
    private GeoUtils(){}

    public static double haversine(double lat1, double lon1, double lat2, double lon2){
        double lat1Rads = Math.toRadians(lat1);
        double lat2Rads = Math.toRadians(lat2);
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a =
                MathUtils.sinPow(dLat / 2, 2) +
                Math.cos(lat1Rads) * Math.cos(lat2Rads)*MathUtils.sinPow(dLon / 2, 2);
        double c = 2*Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = EARTH_RADIUS_METERS*c;
        return d;
    }

    public static double sphericalLawOfCosines(double lat1, double lon1, double lat2, double lon2){
        double lat1Rads = Math.toRadians(lat1);
        double lat2Rads = Math.toRadians(lat2);
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double d =
                Math.acos(
                    Math.sin(lat1Rads)*Math.sin(lat2Rads)
                    + Math.cos(lat1Rads)*Math.cos(lat2Rads)*Math.cos(dLon)
                )*EARTH_RADIUS_METERS;
        return d;
    }

    public static double equirectangularApproximation(double lat1, double lon1, double lat2, double lon2){
        double lat1Rads = Math.toRadians(lat1);
        double lat2Rads = Math.toRadians(lat2);
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double x = dLon*Math.cos((lat1Rads + lat2Rads) / 2);
        double y = dLat;
        double d = Math.sqrt(x*x + y*y) * EARTH_RADIUS_METERS;
        return d;
    }

}
