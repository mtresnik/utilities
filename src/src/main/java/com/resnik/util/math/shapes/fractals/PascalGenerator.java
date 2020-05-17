package com.resnik.util.math.shapes.fractals;

public class PascalGenerator {


    public static int[] generateRow(int ROW){
        int[] ret = new int[ROW + 1];
        ret[0] = 1;
        ret[ROW - 1] = 1;
        for(int i = 1; i <  ret.length; i++){
            ret[i] = binomialCoefficient(ret.length, i);
        }

        return ret;
    }

    public static int binomialCoefficient(int n, int k){
        int res = 1;
        if (k > n - k)
            k = n - k;
        for (int i = 0; i < k; i++){
            res *= (n - i);
            res /= (i + 1);
        }
        return res;
    }


}
