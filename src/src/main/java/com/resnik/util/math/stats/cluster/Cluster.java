package com.resnik.util.math.stats.cluster;

import java.util.ArrayList;
import java.util.List;

public class Cluster {

    List<double[]> points;
    final int DIM;

    public Cluster(double[] seed, double[] ... points){
        this.points = new ArrayList<double[]>(){{this.add(seed);}};
        if(seed == null){
            throw new IllegalArgumentException("Seed must not be null.");
        }
        this.DIM = seed.length;
        this.add(points);
    }

    public boolean add(double[] ... points){
        int c = 0;
        for(double[] point : points){
            if(point == null){
                throw new IllegalArgumentException("Data cannot be null. Non-Seed Index=" + c);
            }
            if(point.length != DIM){
                throw new IllegalArgumentException("Jagged data is not supported. Non-Seed Index=" + c);
            }
            this.points.add(point);
            c++;
        }
        return true;
    }

    public boolean remove(double[] ... points){
        int c = 0;
        for(double[] point : points){
            if(point == null){
                throw new IllegalArgumentException("Data cannot be null. Non-Seed Index=" + c);
            }
            if(point.length != DIM){
                throw new IllegalArgumentException("Jagged data is not supported. Non-Seed Index=" + c);
            }
            this.points.remove(point);
            c++;
        }
        return true;
    }

    public double[] getMean(){
        double[] ret = new double[DIM];
        double[] sum = new double[DIM];
        for(double[] point : points){
            for (int i = 0; i < DIM; i++) {
                sum[i] += point[i];
            }
        }
        for(int i = 0; i < DIM; i++){
            ret[i] = sum[i] / points.size();
        }
        return ret;
    }

    public double distanceTo(double[] point){
        // Euclidean distance
        if(point == null){
            return Double.POSITIVE_INFINITY;
        }
        if(point.length != DIM){
            throw new IllegalArgumentException("Point dimensions not equal. DIM:" + DIM + "!=" + point.length);
        }
        double squaredSum = 0.0;
        double[] mean = getMean();
        for(int i = 0; i < DIM; i++){
            double delta = mean[i] - point[i];
            squaredSum += delta * delta;
        }
        return Math.sqrt(squaredSum);
    }

    public double getVariance(){
        double sum = 0.0;
        for(double[] point : this.points){
            sum += distanceTo(point);
        }
        return sum / points.size();
    }

}
