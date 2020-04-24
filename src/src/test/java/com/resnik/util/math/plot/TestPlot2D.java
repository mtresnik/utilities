package com.resnik.util.math.plot;

import com.resnik.util.math.plot.elements2d.PlotDataset2D;
import com.resnik.util.math.plot.elements2d.PlotElement2D;
import com.resnik.util.math.plot.elements2d.PlotFunction2D;
import com.resnik.util.math.plot.points.Point2d;
import com.resnik.util.math.symbo.algebra.Bounds;
import javafx.scene.paint.Color;

public class TestPlot2D {

    public static void main(String[] args){

        PlotElement2D f1 = new PlotFunction2D((x) -> {
            return x;
        }, Color.BLUE), f2 = new PlotFunction2D((x) -> {
            return x * x;
        }, Color.RED), f3 = new PlotFunction2D((x) -> {
            return Math.sin(x);
        }, Color.GREEN), f4 = new PlotFunction2D((x) -> {
            return Math.cos(x);
        }, Color.YELLOW), d1 = new PlotDataset2D(Point2d.parsePoints(0.0, 1.0, 2.0, 3.0, 6.0, 7.0), Color.WHITE);

        Plot2D.CartesianPlot plot = new Plot2D.CartesianPlot(f1, f2, f3);
        plot.show();
        Plot2D.CartesianPlot plot1 = new Plot2D.CartesianPlot(new Bounds(-2 * Math.PI, 2 * Math.PI), new Bounds(-2, 2), f4);
        plot1.show();

        int max = 70;
        Point2d[] points = new Point2d[max];
        PlotFunction2D gf = new PlotFunction2D((x) -> {
            return x*x;
        }, Color.RED);
        for (int i = 0; i < max; i++) {
            double x = 0.1*(i - max/2);
            double y =  gf.j_function.apply(x)+ 2*(Math.random()  - 0.5);
            points[i] = new Point2d(x, y);
        }
        PlotElement2D ge = new PlotDataset2D(points, Color.WHITE);
        Plot2D.CartesianPlot plot3 = new Plot2D.CartesianPlot(Bounds.DEFAULT_10, Bounds.DEFAULT_10, ge, gf);
        plot3.show();

    }
}
