package com.resnik.util.math.plot.elements3d;

import com.resnik.util.math.plot.points.Point3d;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;

public class Line3d extends PlotElement3D {

    public int thickness;
    public Point3d a,b;

    public Line3d(Color color, int thickness, Point3d a, Point3d b) {
        super(color);
        this.thickness = thickness;
        this.a = new Point3d(a.x.real, -a.y.real, -a.z.real);
        this.b = new Point3d(b.x.real, -b.y.real, -b.z.real);
    }

    @Override
    public Node[] getShapes(Point3d axisRatio, Node o2) {
        double dist = Point3d.dist(a,b).real;
        Line line = new Line(0,0,0,dist);
        line.setStrokeWidth(10);
        line.setStroke(this.color);

        Cylinder cylinder = new Cylinder();
        cylinder.setHeight(dist);
        cylinder.setRadius(0.05);
        cylinder.setScaleX(axisRatio.x.real);
        cylinder.setScaleY(axisRatio.y.real);
        cylinder.setScaleZ(axisRatio.z.real);

        Point3D origin = new Point3D(a.x.real*axisRatio.x.real, a.y.real*axisRatio.y.real, a.z.real*axisRatio.z.real);
        Point3D target = new Point3D(b.x.real*axisRatio.x.real, b.y.real*axisRatio.y.real, b.z.real*axisRatio.z.real);

        cylinder = createConnection(cylinder, origin, target);


        Point3d midPoint = Point3d.midpoint(a,b);

        cylinder.setTranslateX(midPoint.x.real* axisRatio.x.real);
        cylinder.setTranslateY(midPoint.y.real* axisRatio.y.real);
        cylinder.setTranslateZ(midPoint.z.real* axisRatio.z.real);

        PhongMaterial colorMaterial = new PhongMaterial();
        colorMaterial.setDiffuseColor(this.color);
        colorMaterial.setSpecularColor(this.color);
        cylinder.setMaterial(colorMaterial);

        return new Node[]{cylinder};
    }

    public Cylinder createConnection(Cylinder line, Point3D origin, Point3D target) {
        Point3D yAxis = new Point3D(0, 1, 0);
        Point3D diff = target.subtract(origin);

        Point3D mid = target.midpoint(origin);

        Point3D axisOfRotation = diff.crossProduct(yAxis);
        double angle = Math.acos(diff.normalize().dotProduct(yAxis));
        Rotate rotateAroundCenter = new Rotate(-Math.toDegrees(angle), axisOfRotation);

        line.getTransforms().addAll(rotateAroundCenter);

        return line;
    }
}
