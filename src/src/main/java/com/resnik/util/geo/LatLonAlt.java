package com.resnik.util.geo;

public class LatLonAlt extends LatLon {

    double altitude;

    public LatLonAlt(double lat, double lon, double altitude) {
        super(lat, lon);
        this.altitude = altitude;
    }
}
