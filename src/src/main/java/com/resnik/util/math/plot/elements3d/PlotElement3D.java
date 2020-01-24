package com.resnik.util.math.plot.elements3d;

import com.resnik.util.math.plot.points.Point3d;
import com.resnik.util.math.plot.PlotElement;
import javafx.scene.Node;
import javafx.scene.paint.Color;

public abstract class PlotElement3D extends PlotElement<Node, Point3d, Node> {

    public PlotElement3D(Color color) {
        super(color);
    }

    @Override
    public abstract Node[] getShapes(Point3d axisRatio, Node o2);

}
