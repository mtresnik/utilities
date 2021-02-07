package com.resnik.util.math.plot;

import com.resnik.util.math.plot.elements2d.PlotDataset2D;
import com.resnik.util.math.plot.elements2d.PlotElement2D;
import com.resnik.util.math.plot.points.Point2d;
import com.resnik.util.math.symbo.algebra.Bounds;
import javafx.scene.paint.Color;

import java.util.Arrays;

public class TestCalories {


    public static void main(String[] args) {
        int numDays = 100;
        double startingWeight = 186;
        double avgDailyCalories = 1500;
        double[] weights = generateWeight(startingWeight, avgDailyCalories, numDays);
        Point2d[] points = new Point2d[weights.length];
        for(int i = 0; i < weights.length; i++){
            points[i] = new Point2d(i, weights[i]);
        }
        System.out.println(Arrays.toString(weights));
        double[] deltas = new double[weights.length - 1];
        double[] weeklyDeltas = new double[deltas.length / 7];
        for(int i = 0; i < deltas.length - 1; i++){
            deltas[i] = weights[i+ 1] - weights[i];
        }
        for(int i = 0; i < deltas.length - 7; i += 7){
            double sum = 0;
            for(int j = 0; j < 7; j++){
                sum += deltas[i + j];
            }
            weeklyDeltas[i/7] = sum;
        }
        System.out.println(Arrays.toString(deltas));
        System.out.println(Arrays.toString(weeklyDeltas));
        PlotElement2D plotElement2D = new PlotDataset2D(points, Color.WHITE);
        Plot2D.CartesianPlot plot = new Plot2D.CartesianPlot(new Bounds(0, numDays), new Bounds(150, 190), plotElement2D);
        plot.show();
    }

    public static double[] generateWeight(double startingWeight, double calories, int numDays){
        double[] ret = new double[numDays];

        ret[0] = startingWeight;
        for(int i = 1; i < ret.length; i++){
            double m = maintenance(ret[i - 1]);
            double diffCal = m - calories;
            double diffPounds = toPounds(diffCal);
            ret[i] = ret[i - 1] - diffPounds;
        }

        return ret;
    }

    public static double maintenance(double weight){
        return weight * 12;
    }

    public static double toPounds(double calories){
        return calories / 3500;
    }



}
