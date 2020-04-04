package com.resnik.util.objects.structures;

import com.resnik.util.logger.Log;
import org.junit.Test;

public class TestOmniList {

    public static final String TAG = TestOmniList.class.getSimpleName();

    @Test
    public void testOmniList() {
        OmniList<Double> om = new OmniList();
        om.add("test", 0.0, 0.45, 99.0, 55.0);
        om.add("hey", 1.0, 5.0, 0.5, -1.0, 55.9);
        Log.v(TAG,om);
        om.sortSub(Double::compare);
        Log.v(TAG,om);
        om.sort(String.CASE_INSENSITIVE_ORDER);
        Log.v(TAG,om);
        om.add("hey", null);
        Log.v(TAG,om);
    }
}
