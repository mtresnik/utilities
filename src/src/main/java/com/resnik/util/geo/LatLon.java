package com.resnik.util.geo;

public class LatLon {

    double lat, lon;

    public LatLon(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public double distanceTo(LatLon other){
        return GeoUtils.haversine(this, other);
    }
}
