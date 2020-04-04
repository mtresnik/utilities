package com.resnik.util.geo;

import com.resnik.util.logger.Log;
import org.junit.Test;

public class TestGeo {

    public static final String TAG = TestGeo.class.getSimpleName();

    @Test
    public void testDistance(){
        double meters = GeoUtils.haversine(34,-71,35,-71);
        Log.v(TAG,meters);
    }

}
