package com.resnik.util.math.plot.elements3d;

import com.resnik.util.math.plot.points.Point3d;
import javafx.scene.Node;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class PlotDataset3D extends PlotElement3D {

    public Point3d[] points;
    public static final int POINT_SIZE = 10;
    private boolean drawLines, drawPoints;
    private PointType type;

    public PlotDataset3D(Color color, Point3d ... points) {
        super(color);
        this.drawPoints = true;
        this.points = points;
        this.type = PointType.SPHERE;
    }

    public PlotDataset3D(Color color, List<Point3d> list){
        this(color, list.toArray(new Point3d[list.size()]));
    }

    public void setPoints(boolean drawPoints){
        this.drawPoints = drawPoints;
    }

    public void setLines(boolean drawLines){
        this.drawLines = drawLines;
    }

    public void setType(PointType type) {
        this.type = type;
    }

    @Override
    public Node[] getShapes(Point3d o, Node o2) {
        List<Node> retList = new ArrayList();
        for(int i = 0; i < this.points.length; i++){
            Point3d currPoint = this.points[i];
            if(drawPoints){
                switch (this.type){
                    case CUBE:
                        Cube cube = new Cube(this.color, POINT_SIZE, currPoint);
                        retList.add(cube.getShapes(o,o2)[0]);
                        break;
                    case SPHERE:
                    default:
                        Sphere sphere = new Sphere(this.color, POINT_SIZE, currPoint);
                        retList.add(sphere.getShapes(o,o2)[0]);
                }
            }
            if(drawLines){
                if(i < this.points.length - 1){
                    Point3d nextPoint = this.points[i+1];
                    Line3d line = new Line3d(this.color, POINT_SIZE, currPoint, nextPoint);
                    retList.add(line.getShapes(o, o2)[0]);
                }
            }
        }
        return retList.toArray(new Node[retList.size()]);
    }
}
