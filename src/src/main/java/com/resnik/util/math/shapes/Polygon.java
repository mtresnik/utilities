package com.resnik.util.math.shapes;

import com.resnik.util.images.ImageUtils;
import com.resnik.util.logger.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class Polygon implements Shape {


    List<PolygonPoint> polygonPoints;

    public Polygon(List<PolygonPoint> points){
        this.polygonPoints = points;
        Polygon temp = convexHull(points);
        this.polygonPoints = temp.polygonPoints;
    }

    private Polygon(){
    }

    @Override
    public boolean contains(PolygonPoint point) {
        if(!this.toBoundingBox().contains(point)){
            return false;
        }
        int npoints = polygonPoints.size();
        int[] xPoints = new int[npoints];
        int[] yPoints = new int[npoints];

        double mult = 1e3;

        for(int i = 0; i < polygonPoints.size(); i++){
            xPoints[i] = (int)(polygonPoints.get(i).x * mult);
            yPoints[i] = (int)(polygonPoints.get(i).y * mult);
        }
        java.awt.Polygon innerPolygon = new java.awt.Polygon(xPoints, yPoints, npoints);
        return innerPolygon.contains(point.x*mult, point.y*mult);
    }

    @Override
    public List<PolygonPoint> getPoints() {
        return polygonPoints;
    }


    private static double crossProduct(PolygonPoint p, PolygonPoint q, PolygonPoint i){
        PolygonPoint pq = new PolygonPoint(q.x - p.x, q.y - p.y);
        PolygonPoint pi = new PolygonPoint(i.x - p.x, i.y - p.y);
        return pq.x*pi.y - pq.y*pi.x;
    }

    // Refs https://www.geeksforgeeks.org/convex-hull-set-1-jarviss-algorithm-or-wrapping/
    public static Polygon convexHull(List<PolygonPoint> points){
        // There must be at least 3 points
        final int n = points.size();
        if (n < 3) throw new IllegalArgumentException("Must have at least 3 points.");

        // Initialize Result
        List<PolygonPoint> hull = new ArrayList<>();

        int leftMost = 0;
        for(int i = 1; i < points.size(); i++){
            if(points.get(i).x < points.get(leftMost).x){
                leftMost = i;
            }
        }
        boolean[] visitedIndices = new boolean[points.size()];
        int curr = leftMost;
        int next = 0;
        do{
            hull.add(points.get(curr));
            visitedIndices[curr] = true;
            next = (curr + 1)%points.size();
            for(int i = 0; i < points.size(); i++){
                double crossProduct = crossProduct(points.get(curr), points.get(next), points.get(i));
                if(crossProduct < 0){
                    next = i;
                }else if(crossProduct == 0 && points.get(curr).distance(points.get(i)) > points.get(curr).distance(points.get(next))){
                    next=i;
                }
            }
            for(int i=0;i<points.size();i++){
                if(i!=next&&
                        i!=curr&&
                        crossProduct(points.get(curr),points.get(next),points.get(i))==0&&
                        visitedIndices[i]==false){
                    visitedIndices[i]=true;
                    hull.add(points.get(i));
                }
            }
            curr = next;
        }while (next != leftMost);

        Polygon ret = new Polygon();
        ret.polygonPoints = hull;
        return ret;
    }

    public List<Polygon> split(int n){
        // Equal sized polygons (unit vector = <1, 1, 1, ...> / sqrt(n))
        if(n == 1){
            return Collections.singletonList(this);
        }
        List<Polygon> ret = new ArrayList<>();
        List<PolygonPoint> outerPoints = new ArrayList<>(this.polygonPoints);
        double subArea = this.getArea() / n;
        double maxArea = this.getArea();
        List<PolygonPoint> toAdd = new ArrayList<>();
        double t = 0.5;
        for(int curr = 0; curr < outerPoints.size(); curr++){
            int next = (curr + 1) % outerPoints.size();
            PolygonPoint currPoint = outerPoints.get(curr), nextPoint = outerPoints.get(next);
            double newX = (nextPoint.x - currPoint.x)*t + currPoint.x;
            double newY = (nextPoint.y - currPoint.y)*t + currPoint.y;
            toAdd.add(new PolygonPoint(newX, newY));
        }
        outerPoints.addAll(toAdd);
        outerPoints = new Polygon(outerPoints).polygonPoints;
        for(int POLYGON = 0; POLYGON < n; POLYGON++){
            Log.v("Polygon", POLYGON);
            Log.v("Polygon", outerPoints);
            int left = -1 + outerPoints.size();
            if(outerPoints.size() < 3){
                break;
            }
            outerPoints = new Polygon(outerPoints).polygonPoints;
            int right = 1;
            int minIndex = left;
            int maxIndex = right;
            List<PolygonPoint> subPoints = new ArrayList<>();
            Shape subPolygon = new BoundingBox(0,0,0,0);
            while (left != right && subPolygon.getArea() < subArea && subPolygon.getArea() > maxArea){
                subPoints.clear();
                left = (left - 1 + outerPoints.size()) % outerPoints.size();
                right = (right + 1) % outerPoints.size();
                minIndex = Math.min(left, right);
                maxIndex = Math.max(left, right);
                for(int i = minIndex; i <= maxIndex; i++){
                    subPoints.add(outerPoints.get(i));
                }
                if(subPoints.size() < 3){
                    Log.e("Polygon", "Sub points < 3. Continuing.");
                    continue;
                }
                subPolygon = new Polygon(subPoints);
            }
            PolygonPoint addBack = outerPoints.get(minIndex);
            PolygonPoint addBackMax = outerPoints.get(maxIndex);
            outerPoints.removeAll(subPoints);
            outerPoints.add(addBack);
            outerPoints.add(addBackMax);
            if(subPolygon.getArea() > 0 && subPolygon instanceof Polygon){
                ret.add((Polygon) subPolygon);
            }
        }
        if(outerPoints.size() >= 3){
            ret.add(new Polygon(outerPoints));
        }
        return ret;
    }

    public List<PolygonPoint> toPointCloud(double dx, double dy){
        List<PolygonPoint> innerCloud = new ArrayList<>();
        for(double x = this.minX(); x <= this.maxX(); x+= dx){
            for(double y = this.minY(); y <= this.maxY(); y+=dy){
                PolygonPoint test = new PolygonPoint(x, y);
                if(this.contains(test)){
                    innerCloud.add(test);
                }
            }
        }
        Log.e("Polygon", "PointCloudSize=" + innerCloud.size());
        return innerCloud;
    }

    public List<Polygon> splitPointCloud(int n){
        int ratio = 200;
        double dx = (this.maxX() - this.minX()) / ratio;
        double dy = (this.maxY() - this.minY()) / ratio;
        List<PolygonPoint> pointCloud = toPointCloud(dx, dy);
        int offsetX = (int) this.minX();
        int offsetY = (int) this.minY();
        int maxCol = (int)(this.maxX() - offsetX);
        int maxRow = (int)(this.maxY() - offsetY);
        byte[][][] image = new byte[maxRow][maxCol][];
        for(int ROW = 0; ROW < maxRow; ROW++){
            for(int COL = 0; COL < maxCol; COL++){
                image[ROW][COL] = ImageUtils.WHITE_ARGB;
            }
        }
        for(PolygonPoint polygonPoint : pointCloud){
            try{
                image[(int)polygonPoint.y][(int) polygonPoint.x] = ImageUtils.BLACK_ARGB;
            }catch (Exception e){
            }
        }
        try {
            ImageUtils.saveImageBytes(image, "res/pointCloud.bmp");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.singletonList(this);
    }

    @Override
    public String toString() {
        return "Polygon{ size=" + this.polygonPoints.size()+ "}";
    }
}
