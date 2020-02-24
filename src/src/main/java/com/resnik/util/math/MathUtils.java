package com.resnik.util.math;

import com.resnik.util.objects.arrays.ArrayUtils;

import java.math.MathContext;
import java.math.RoundingMode;
import java.util.function.Function;

public final class MathUtils {
    public static final MathContext mc = new MathContext(128, RoundingMode.HALF_EVEN);

    private MathUtils() {
    }
    
    public static double sign(double d){
        return Double.compare(d, 0);
    }

    public static int mult(int[] nums){
        int ret = 1;
        for(int i : nums){
            ret*=i;
        }
        return ret;
    }

    public static int sum(int[] nums){
        int ret = 0;
        for(int i : nums){
            ret+=i;
        }
        return ret;
    }

    public static int[] digits(int num){
        String rep = Integer.toString(num);
        char[] chars = rep.toCharArray();
        int[] retArray = new int[chars.length];
        for(int i = 0; i < chars.length; i++){
            retArray[i] = Integer.parseInt(Character.toString(chars[i]));
        }
        return retArray;
    }

    public static double sinPow(double x, double pow){
        return Math.pow(Math.sin(x), pow);
    }

    public static double cosPow(double x, double pow){
        return Math.pow(Math.cos(x), pow);
    }

    public static double cos(double[] a, double[] b){
        if(a == null || b == null || a.length != b.length){
            throw new IllegalArgumentException("Must pass non-null arrays of same length.");
        }
        double dot = 0.0;
        double aSquaredSum = 0.0;
        double bSquaredSum = 0.0;
        for (int i = 0; i < a.length; i++) {
            dot += a[i]*b[i];
            aSquaredSum += a[i] * a[i];
            bSquaredSum += b[i] * b[i];
        }
        double aMag = Math.sqrt(aSquaredSum);
        double bMag = Math.sqrt(bSquaredSum);
        // a . b = |a|*|b|*cos(a^b)
        return dot / (aMag * bMag);
    }

    public static double cos(int[] a, int[] b){
        return cos(ArrayUtils.intsToDoubles(a), ArrayUtils.intsToDoubles(b));
    }

    public static double theta(double[] a, double[] b){
        return Math.acos(cos(a,b));
    }

    public static double theta(int[] a, int[] b){
        return theta(ArrayUtils.intsToDoubles(a), ArrayUtils.intsToDoubles(b));
    }

    public static double sin(double[] a, double[] b){
        return Math.sin(theta(a, b));
    }

    public static double sin(int[] a, int[] b){
        return sin(ArrayUtils.intsToDoubles(a), ArrayUtils.intsToDoubles(b));
    }

    public static Function<Double, Double> funcPow(Function<Double, Double> function, double pow){
        return (a) -> Math.pow(function.apply(a), pow);
    }

    public static double applyFuncPow(double eval, Function<Double, Double> function, double pow){
        return funcPow(function, pow).apply(eval);
    }

}
