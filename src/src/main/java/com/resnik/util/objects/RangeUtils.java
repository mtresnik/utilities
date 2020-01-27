package com.resnik.util.objects;

public class RangeUtils {

    public static boolean inRange(double query, double x1, double x2){
        double max = Math.max(x1,x2);
        double min = Math.min(x1,x2);
        return query >= min && query <= max;
    }

}
