package com.resnik.util.math.shapes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BoundingBox implements Shape {

    List<PolygonPoint> polygonPoints;
    PolygonPoint minPoint;
    PolygonPoint maxPoint;

    public BoundingBox(double x1, double y1, double x2, double y2){
        this(new PolygonPoint(x1, y1), new PolygonPoint(x2,y2));
    }

    public BoundingBox(List<PolygonPoint> polygonPoints){
        if(polygonPoints == null || polygonPoints.size() < 2){
            throw new IllegalArgumentException("Must be non-null and of length 2 or more.");
        }
        this.polygonPoints = polygonPoints;
        minPoint = new PolygonPoint(this.minX(), this.minY());
        maxPoint = new PolygonPoint(this.maxX(), this.maxY());
        this.polygonPoints = new ArrayList<>(Arrays.asList(minPoint, maxPoint));
    }

    public BoundingBox(PolygonPoint ... points){
        this(Arrays.asList(points));
    }

    @Override
    public boolean contains(PolygonPoint point) {
        return point.x >= minPoint.x && point.x <= maxPoint.x && point.y >= minPoint.y && point.y <= maxPoint.y;
    }

    @Override
    public List<PolygonPoint> getPoints() {
        return this.polygonPoints;
    }

    @Override
    public BoundingBox toBoundingBox(){
        return this;
    }

    public double getArea(){
        double dx = maxPoint.x - minPoint.x;
        double dy = maxPoint.y - minPoint.y;
        return dx*dy;
    }

    public PolygonPoint getCenter(){
        return new PolygonPoint((maxX() + minX())/2, (maxY() + minY())/2);
    }
}
