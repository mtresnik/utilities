package com.resnik.util.math.shapes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Triangle implements Shape {

    private List<PolygonPoint> polygonPoints;
    private PolygonPoint p1, p2, p3;

    public Triangle(PolygonPoint p1, PolygonPoint p2, PolygonPoint p3){
        this.polygonPoints = new ArrayList<>(Arrays.asList(p1,p2,p3));
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
    }

    @Override
    public boolean contains(PolygonPoint point) {
        Triangle t1 = new Triangle(p1, p2, point);
        Triangle t2 = new Triangle(p2, p3, point);
        Triangle t3 = new Triangle(p3, p1, point);
        return (t1.getArea() + t2.getArea() + t3.getArea()) == getArea();
    }

    @Override
    public List<PolygonPoint> getPoints() {
        return polygonPoints;
    }

    @Override
    public double getArea() {
        double a = p1.distance(p2);
        double b = p2.distance(p3);
        double c = p3.distance(p1);
        double s = (a + b + c) /2;
        return Math.sqrt(s*(s - a)*(s - b)*(s - c));
    }
}
