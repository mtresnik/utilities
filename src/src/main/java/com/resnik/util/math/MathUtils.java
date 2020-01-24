package com.resnik.util.math;

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

    public static Function<Double, Double> funcPow(Function<Double, Double> function, double pow){
        return (a) -> Math.pow(function.apply(a), pow);
    }

    public static double applyFuncPow(double eval, Function<Double, Double> function, double pow){
        return funcPow(function, pow).apply(eval);
    }
}
