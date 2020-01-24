package com.resnik.util.math.plot.elements2d;

import com.resnik.util.math.plot.Plot2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

import java.util.function.Function;

import static com.resnik.util.math.plot.Plot2D.CartesianPlot.Plot.mapX;
import static com.resnik.util.math.plot.Plot2D.CartesianPlot.Plot.mapY;

public class PlotFunction2D extends PlotElement2D {

    public Function<Double, Double> j_function;

    public PlotFunction2D(Function<Double, Double> j_function, Color color) {
        super(color);
        this.j_function = j_function;
    }

    @Override
    public Shape[] getShapes(Plot2D.CartesianPlot.Axes axes, Plot2D.CartesianPlot.Plot plot) {
        Path path = new Path();
        path.setStroke(color.deriveColor(0, 1, 1, 0.6));
        path.setStrokeWidth(2);

        path.setClip(
                new Rectangle(
                        0, 0,
                        axes.getPrefWidth(),
                        axes.getPrefHeight()
                )
        );

        double x = axes.xLow;
        double y = j_function.apply(x);

        path.getElements().add(
                new MoveTo(
                        mapX(x, axes), mapY(y, axes)
                )
        );

        x += plot.xInc;
        while (x < plot.xMax) {
            y = j_function.apply(x);

            path.getElements().add(
                    new LineTo(
                            mapX(x, axes), mapY(y, axes)
                    )
            );

            x += plot.xInc;
        }
        return new Shape[]{path};
    }

}
