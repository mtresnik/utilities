package com.resnik.util.math.plot.elements3d;

import com.resnik.util.files.xml.XMLElement;
import com.resnik.util.files.xml.XMLNode;
import com.resnik.util.math.plot.points.Point3d;
import com.resnik.util.objects.structures.tree.TreeNode;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;

import java.util.List;

public class Line3d extends PlotElement3D {

    public static final String NAME = "line3d";

    public int thickness;
    public Point3d a,b;
    public int aIndex=-1, bIndex=-1;

    public Line3d(Color color, int thickness, Point3d a, Point3d b) {
        super(color);
        this.thickness = thickness;
        this.a = a;
        this.b = b;
        this.justifyPoints();
    }

    public Line3d(Color color, Point3d a, Point3d b) {
        this(color, PlotLineSet3D.POINT_SIZE, a, b);
    }

    private Line3d(Color color) {
        super(color);
        this.thickness = PlotLineSet3D.POINT_SIZE;
    }

    public void justifyPoints(){
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

    public static Line3d fromXMLNode(XMLNode element){
        if(!element.getName().equals(NAME)){
            return null;
        }
        int aIndex = Integer.parseInt(((XMLElement)element.getValue()).getOrDefault("aIndex", "-1").toString());
        int bIndex = Integer.parseInt(((XMLElement)element.getValue()).getOrDefault("bIndex", "-1").toString());
        String colorString = ((XMLElement)element.getValue()).getOrDefault("color", "black").toString();
        Color color = Color.web(colorString);
        List<TreeNode<XMLElement>> children = element.getChildren();
        Point3d a=null, b=null;
        for(TreeNode<XMLElement> child : children){
            if(!(child instanceof XMLNode)) continue;
            XMLNode childNode = (XMLNode) child;
            switch (childNode.getName()){
                case Point3d.NAME:
                    if(a == null){
                        a = Point3d.fromXMLNode((XMLNode) childNode.getChildren().get(0));
                        continue;
                    }
                    if(b == null){
                        b = Point3d.fromXMLNode((XMLNode) childNode.getChildren().get(0));
                        continue;
                    }
                    break;
                default:
            }
        }
        Line3d retLine = new Line3d(color);
        retLine.a = a;
        retLine.b = b;
        retLine.aIndex = aIndex;
        retLine.bIndex = bIndex;
        return retLine;
    }

    public double dist(){
        return Point3d.dist(a, b).real;
    }
}
