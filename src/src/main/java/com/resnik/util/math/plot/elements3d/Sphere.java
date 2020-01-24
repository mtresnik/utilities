package com.resnik.util.math.plot.elements3d;

import com.resnik.util.math.plot.points.Point3d;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Shape3D;

public class Sphere extends PlotElement3D {

    public double width;
    public Point3d center;

    public Sphere(Color color, double width, Point3d center) {
        super(color);
        this.width = width;
        this.center = new Point3d(center.x.real, -center.y.real, -center.z.real);
    }

    @Override
    public Node[] getShapes(Point3d axisRatio, Node o2) {
        Shape3D sphere = new javafx.scene.shape.Sphere(this.width);
        PhongMaterial colorMaterial = new PhongMaterial();
        colorMaterial.setDiffuseColor(this.color);
        colorMaterial.setSpecularColor(this.color);
        sphere.setTranslateX(this.center.x.real*axisRatio.x.real);
        sphere.setTranslateY(this.center.y.real*axisRatio.y.real);
        sphere.setTranslateZ(this.center.z.real*axisRatio.z.real);
        sphere.setMaterial(colorMaterial);
        return new Node[]{sphere};
    }
}
