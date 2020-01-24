package com.resnik.util.math.polygons;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PolygonPoint implements Comparable<PolygonPoint> {

    public final double x, y;

    public PolygonPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public PolygonPoint(double[] coords) {
        this(coords[0], coords[1]);
    }

    public PolygonPoint(int[] coords) {
        this(coords[0], coords[1]);
    }

    public PolygonPoint(float[] coords) {
        this(coords[0], coords[1]);
    }

    public String toString() {
        return "(" + x + "," + y + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PolygonPoint)) {
            return false;
        } else {
            PolygonPoint other = (PolygonPoint) obj;
            return x == other.x && y == other.y;
        }
    }

    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public int compareTo(PolygonPoint other) {
        if (x != other.x) {
            return Double.compare(x, other.x);
        } else {
            return Double.compare(y, other.y);
        }
    }

    public static List<PolygonPoint> generatePoints(double[][] pointArr) {
        List<PolygonPoint> retList = new ArrayList();
        for (int ROW = 0; ROW < pointArr.length; ROW++) {
            PolygonPoint currPoint = new PolygonPoint(pointArr[ROW]);
            retList.add(currPoint);
        }
        return retList;
    }

    public static List<PolygonPoint> generatePoints(int[][] pointArr) {
        List<PolygonPoint> retList = new ArrayList();
        for (int ROW = 0; ROW < pointArr.length; ROW++) {
            PolygonPoint currPoint = new PolygonPoint(pointArr[ROW]);
            retList.add(currPoint);
        }
        return retList;
    }

    public static List<PolygonPoint> generatePoints(float[][] pointArr) {
        List<PolygonPoint> retList = new ArrayList();
        for (int ROW = 0; ROW < pointArr.length; ROW++) {
            PolygonPoint currPoint = new PolygonPoint(pointArr[ROW]);
            retList.add(currPoint);
        }
        return retList;
    }

}
