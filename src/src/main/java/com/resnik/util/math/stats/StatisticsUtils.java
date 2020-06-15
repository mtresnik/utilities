package com.resnik.util.math.stats;

import java.util.List;

public class StatisticsUtils {

    public static double E(double[] p, double[] x){
        if(x == null || p == null){
            throw new IllegalArgumentException("X != null && p != null");
        }
        if(x.length != p.length){
            throw new IllegalArgumentException("X.length != p.length");
        }
        int n = x.length;
        double sum = 0.0;
        for(int i = 0; i < n; i++){
            sum += p[i] * x[i];
        }
        return sum;
    }

    public static double G(double[] x){
        double n = x.length;
        double product = 1.0;
        for(int i = 0; i < n; i++){
            product *= x[i];
        }
        return Math.pow(product, 1 / n);
    }

    public static double mean(List<Double> values){
        double[] newVals = new double[values.size()];
        for(int i = 0; i < values.size(); i++){
            newVals[i] = values.get(i);
        }
        return mean(newVals);
    }

    public static double mean(double ... values){
        double sum = 0.0;
        int n = values.length;
        if(n == 0){
            return 0;
        }
        for(double v : values){
            sum+=v;
        }
        return sum / n;
    }

    public static double var(double ... values){
        double mean = mean(values);
        double sum = 0.0;
        int n = values.length;
        if(n <= 1){
            return 0;
        }
        for(double v : values){
            sum+= Math.pow(v - mean, 2);
        }
        return sum / (n - 1);
    }

    public static double varSum(double[] x, double[] y){
        double ret = 0.0;
        ret += var(x);
        ret += var(y);
        ret += 2*cov(x, y);
        return ret;
    }

    public static double cov(double[] x, double[] y){
        if(x == null || y == null){
            throw new IllegalArgumentException("X != null && Y != null");
        }
        if(x.length != y.length){
            throw new IllegalArgumentException("X.length != Y.length");
        }
        int n = x.length;
        if(n <= 1){
            return 0;
        }
        double xMean = mean(x);
        double yMean = mean(y);
        double sum = 0.0;
        for(int i = 0; i < n; i++){
            sum += (x[i] - xMean) * (y[i] - yMean);
        }
        return sum / (n - 1);
    }

    public static double cor(double[] x, double[] y){
        double cov = cov(x, y);
        double varX = var(x);
        double varY = var(y);
        return cov / Math.sqrt(varX * varY);
    }

    public static double std(double ... values){
        return Math.sqrt(var(values));
    }


    public static double std(List<Double> values){
        double[] newVals = new double[values.size()];
        for(int i = 0; i < values.size(); i++){
            newVals[i] = values.get(i);
        }
        return std(newVals);
    }

}
