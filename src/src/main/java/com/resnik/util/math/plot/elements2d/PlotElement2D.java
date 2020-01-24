package com.resnik.util.math.plot.elements2d;

import com.resnik.util.math.plot.Plot2D;
import com.resnik.util.math.plot.PlotElement;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

public abstract class PlotElement2D extends PlotElement<Shape, Plot2D.CartesianPlot.Axes, Plot2D.CartesianPlot.Plot> {

    public PlotElement2D(Color color) {
        super(color);
    }

    @Override
    public abstract Shape[] getShapes(Plot2D.CartesianPlot.Axes axes, Plot2D.CartesianPlot.Plot plot);

}
