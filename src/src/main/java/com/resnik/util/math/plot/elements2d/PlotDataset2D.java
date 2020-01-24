package com.resnik.util.math.plot.elements2d;

import com.resnik.util.math.plot.points.Point2d;
import com.resnik.util.math.plot.Plot2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

import static com.resnik.util.math.plot.Plot2D.CartesianPlot.Plot.mapX;
import static com.resnik.util.math.plot.Plot2D.CartesianPlot.Plot.mapY;

public class PlotDataset2D extends PlotElement2D {

    public Point2d[] points;

    public PlotDataset2D(Point2d[] points, Color color) {
        super(color);
        this.points = points;
    }

    @Override
    public Shape[] getShapes(Plot2D.CartesianPlot.Axes axes, Plot2D.CartesianPlot.Plot plot) {
        Shape[] retArray = new Shape[points.length];
        for (int i = 0; i < retArray.length; i++) {
            Point2d curr = this.points[i];
            Circle c = new Circle(mapX(curr.x.real, axes), mapY(curr.y.real, axes), 4);
            c.setStroke(this.color);
            c.setFill(Color.TRANSPARENT);
            retArray[i] = c;
        }
        return retArray;
    }

}
