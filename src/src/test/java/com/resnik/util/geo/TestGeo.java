package com.resnik.util.geo;

import org.junit.Test;

public class TestGeo {

    @Test
    public void testDistance(){
        double meters = GeoUtils.haversine(34,-71,35,-71);
        System.out.println(meters);
    }

}
