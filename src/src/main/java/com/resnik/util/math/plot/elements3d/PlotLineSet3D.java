package com.resnik.util.math.plot.elements3d;

import com.resnik.util.files.xml.XMLNode;
import com.resnik.util.math.plot.points.Point3d;
import javafx.scene.Node;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class PlotLineSet3D extends PlotElement3D {

    private List<Point3d> points;
    private List<Line3d> lines;
    private PointType type;
    public static final int POINT_SIZE = 10;
    public static final String NAME = "plotlineset3d";

    public PlotLineSet3D() {
        super(Color.BLACK);
        points = new ArrayList<>();
        lines = new ArrayList<>();
    }

    public boolean addPoint(Point3d point3d){
        return points.add(point3d);
    }

    public void setPoints(List<Point3d> points) {
        this.points = points;
    }

    public boolean addLine(Line3d line){
        return lines.add(line);
    }

    public void setLines(List<Line3d> lines) {
        this.lines = lines;
    }

    public void justify(){
        // Ensure all lines have either connecting points or non floating indices
        Function<Integer, Boolean> indexValidator = (i) -> i < points.size() && i >= 0;
        List<Line3d> toRemove = new ArrayList<>();
        for(Line3d line : lines){
            boolean validAIndex = indexValidator.apply(line.aIndex);
            if(line.a == null && !validAIndex){
                toRemove.add(line);
                continue;
            }
            if(validAIndex){
                line.a = points.get(line.aIndex);
            }
            boolean validBIndex = indexValidator.apply(line.bIndex);
            if(line.b == null && !validBIndex){
                toRemove.add(line);
                continue;
            }
            if(validBIndex){
                line.b = points.get(line.bIndex);
            }
            line.justifyPoints();
        }
        lines.removeAll(toRemove);
    }

    @Override
    public Node[] getShapes(Point3d axisRatio, Node o2) {
        List<Node> retList = new ArrayList();
        if(this.type == null){
            this.type = PointType.SPHERE;
        }
        for(int i = 0; i < this.points.size(); i++){
            Point3d currPoint = this.points.get(i);
            Color currColor = currPoint.color == null ? this.color : currPoint.color;
            switch (this.type){
                case CUBE:
                    Cube cube = new Cube(currColor, this.type.size, currPoint);
                    retList.add(cube.getShapes(axisRatio,o2)[0]);
                    break;
                case SPHERE:
                default:
                    Sphere sphere = new Sphere(currColor, this.type.size, currPoint);
                    retList.add(sphere.getShapes(axisRatio,o2)[0]);
            }
        }
        for(Line3d currLine : this.lines){
            retList.add(currLine.getShapes(axisRatio, o2)[0]);
        }
        return retList.toArray(new Node[retList.size()]);
    }

    public static PlotLineSet3D fromXMLNode(XMLNode node){
        if(!node.getName().equals(NAME)) return null;
        PlotLineSet3D retPlot = new PlotLineSet3D();
        for(Object child : node.getChildren()){
            if(!(child instanceof XMLNode)) continue;
            XMLNode _child = (XMLNode) child;
            switch (((XMLNode) child).getName()){
                case "points":
                    for(Object grandChild : _child.getChildren()){
                        retPlot.addPoint(Point3d.fromXMLNode((XMLNode) grandChild));
                    }
                    break;
                case "lines":
                    for(Object grandChild : _child.getChildren()){
                        retPlot.addLine(Line3d.fromXMLNode((XMLNode) grandChild));
                    }
                    break;
                default:
                    continue;
            }
        }
        retPlot.justify();
        return retPlot;
    }

    @Override
    public String toString() {
        return "PlotLineSet3D{" +
                "points=" + points +
                ", lines=" + lines +
                ", type=" + type +
                '}';
    }
}
