package com.resnik.util.logger;

import org.junit.Test;

import java.io.File;

public class TestLog {

    public static final String TAG = TestLog.class.getSimpleName();

    @Test
    public void testLog(){
        Log.i(TAG, "message");
        Log.d(TAG, "message");
        Log.e(TAG, "message");
        Log.v(TAG, "message");
    }

}
