package com.resnik.util.math.plot;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;

public abstract class PlotElement<SHAPE extends Node, AXES, PLOT> {

    public Color color;

    public PlotElement(Color color) {
        this.color = color;
    }

    public abstract SHAPE[] getShapes(AXES axes, PLOT plot);

    public Group getGroup(AXES axes, PLOT plot){
        Group group = new Group();
        group.getChildren().addAll(this.getShapes(axes, plot));
        return group;
    }

}
