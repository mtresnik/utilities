package com.resnik.util.objects;

import com.resnik.util.logger.Log;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;

public class TestFilter {

    @Test
    public void testFilter1(){
        Filter<Double> filter = (x) -> x > 2;
        Collection<Double> sorted = filter.apply(Arrays.asList(1.0,2.0,3.0,4.0,5.0));
        Log.d("TestFilter", sorted);
    }

}
