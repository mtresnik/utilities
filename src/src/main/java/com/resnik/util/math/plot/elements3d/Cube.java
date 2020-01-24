package com.resnik.util.math.plot.elements3d;

import com.resnik.util.math.plot.points.Point3d;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Shape3D;

import java.util.ArrayList;
import java.util.List;

public class Cube extends PlotElement3D {

    public double width;
    public Point3d center;

    public Cube(Color color, double width, Point3d center) {
        super(color);
        this.width = width;
        this.center = new Point3d(center.x.real, -center.y.real, -center.z.real);
    }

    @Override
    public Shape3D[] getShapes(Point3d axisRatio, Node o2) {
        List<Shape3D> cubeFaces = new ArrayList<>();
        Box box = new Box(width, width, width);
        PhongMaterial colorMaterial = new PhongMaterial();
        colorMaterial.setDiffuseColor(this.color);
        colorMaterial.setSpecularColor(this.color);
        box.setTranslateX(this.center.x.real*axisRatio.x.real);
        box.setTranslateY(this.center.y.real*axisRatio.y.real);
        box.setTranslateZ(this.center.z.real*axisRatio.z.real);
        box.setMaterial(colorMaterial);
        cubeFaces.add(box);
        return cubeFaces.toArray(new Shape3D[cubeFaces.size()]);
    }

}
