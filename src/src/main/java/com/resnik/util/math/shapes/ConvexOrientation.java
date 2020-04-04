package com.resnik.util.math.shapes;

import com.resnik.util.images.ImageUtils;
import com.resnik.util.logger.Log;

import java.util.*;

@Deprecated
public class ConvexOrientation {

    public static final String TAG = ConvexOrientation.class.getSimpleName();

    private double minX, minY, maxX, maxY, midX, midY;
    private Map<Double, Double> thetaMap;
    private Map<Double, PolygonPoint> edgeMap;
    public List<PolygonPoint> edgePoints;
    private List<PolygonPoint> basePoints;

    public ConvexOrientation(List<PolygonPoint> points) {
        minX = minY = Double.MAX_VALUE;
        maxX = maxY = -minX;
        for (PolygonPoint point : points) {
            minX = Math.min(minX, point.x);
            minY = Math.min(minY, point.y);
            maxX = Math.max(maxX, point.x);
            maxY = Math.max(maxY, point.y);
        }
        midX = (maxX + minX) / 2.0;
        midY = (maxY + minY) / 2.0;
        Log.v(TAG,Arrays.asList(minX, midX, maxX, minY, midY, maxY));
        thetaMap = new LinkedHashMap();
        edgeMap = new LinkedHashMap();
        this.basePoints = points;
        this.initThetaMap();
        Log.v(TAG,this.thetaMap);
    }

    private void initThetaMap() {
        this.basePoints.sort(new Comparator<PolygonPoint>() {
            @Override
            public int compare(PolygonPoint t, PolygonPoint t1) {
                return Double.compare(getTheta(t), getTheta(t1));
            }
        });
        this.basePoints.forEach((p) -> {
            double theta = getTheta(p);
            double dx = p.x - midX;
            double dy = p.y - midY;
            double dist = Math.sqrt(dx * dx + dy * dy);
            double maxDist = thetaMap.getOrDefault(theta, -1.0);
            if (dist > maxDist) {
                thetaMap.put(theta, dist);
            }
        });
        this.edgePoints = new ArrayList();
        double minDist, maxDist;
        double minTheta = minDist = Double.MAX_VALUE;
        double maxTheta = maxDist = -minTheta;
        this.edgePoints = new ArrayList();
        for(Map.Entry<Double, Double> entry : thetaMap.entrySet()){
            minTheta = Math.min(minTheta, entry.getKey());
            maxTheta = Math.max(maxTheta, entry.getKey());
            minDist = Math.min(minDist, entry.getValue());
            maxDist = Math.max(maxDist, entry.getValue());
        }

        for(Map.Entry<Double, Double> entry : thetaMap.entrySet()){
            if(Math.abs(entry.getValue() - maxDist) < 3){
                PolygonPoint currPoint = this.getPoint(entry);
                this.edgePoints.add(currPoint);
            }
        }
        this.edgePoints.sort(new Comparator<PolygonPoint>(){
            @Override
            public int compare(PolygonPoint t, PolygonPoint t1) {
                return Double.compare(getTheta(t), getTheta(t1));
            }
        });
        this.edgePoints.remove(0);
        for(PolygonPoint currPoint : this.edgePoints){
            Log.v(TAG,currPoint + "\t" + this.getTheta(currPoint));
        }
        Log.v(TAG,Arrays.asList(minTheta, maxTheta) + "\t" + Arrays.asList(minDist, maxDist));
        Log.v(TAG,this.edgePoints);

    }

    private PolygonPoint getPoint(Map.Entry<Double,Double> entry){
        return getPoint(entry.getKey(), entry.getValue());
    }

    private PolygonPoint getPoint(double theta, double distance){
        double dx = Math.cos(theta);
        double dy = Math.sin(theta);
        double x = dx*distance + this.midX;
        double y = dy*distance + this.midY;
        return new PolygonPoint(x, y);
    }

    private double getDist(PolygonPoint p){
        double dx = midX - p.x;
        double dy = midY - p.y;
        double dist = Math.sqrt(dx * dx + dy * dy);
        return dist;
    }

    private double getTheta(PolygonPoint p) {
        double dx = midX - p.x;
        double dy = midY - p.y;
        double dist = Math.sqrt(dx * dx + dy * dy);
        double theta;
        if (dist == 0) {
            theta = 0;
        } else {
            theta = Math.atan(dy / dx);
            int quad = getQuadrant(dx, dy);
            if(quad == 2 || quad == 3){
                theta += Math.PI;
            }
        }
        if(theta < 0){
            theta += 2*Math.PI;
        }
        if(theta >= 2*Math.PI){
            theta -= 2*Math.PI;
        }
        final int PLACES = 1;
        theta = Math.floor(theta * Math.pow(10, PLACES)) / Math.pow(10, PLACES);
        return theta;
    }

    private static int getQuadrant(double x, double y){
        if(x > 0 && y > 0){
            return 1;
        }
        if(x < 0 && y > 0){
            return 2;
        }
        if(x < 0 && y < 0){
            return 3;
        }
        if(x > 0 && y < 0){
            return 4;
        }
        return -1;
    }

    public boolean contains(double x, double y){
        return contains(new PolygonPoint(x, y));
    }

    public boolean contains(PolygonPoint p){
        double dx = midX - p.x;
        double dy = midY - p.y;
        double dist = Math.sqrt(dx * dx + dy * dy);
        double theta = getTheta(p);
        if(this.thetaMap.containsKey(theta)){
            if(thetaMap.get(theta) >= dist){
                return true;
            }
        }
        if (p.x > maxX || p.x < minX || p.y > maxY || p.y < minY) {
            return false;
        }
        List<Map.Entry<Double, Double>> iter = new ArrayList(this.thetaMap.entrySet());
        for(int i =0; i < this.thetaMap.size() ; i++){
            Map.Entry<Double, Double> currEntry = iter.get(i);
            Map.Entry<Double, Double> nextEntry = (i == iter.size() - 1 ? iter.get(0) : iter.get(i + 1) );
            double currTheta = currEntry.getKey(); double currDist = currEntry.getValue();
            double nextTheta = nextEntry.getKey(); double nextDist = nextEntry.getValue();
            if((theta > currTheta && theta < nextTheta) || (theta > nextTheta && theta < currTheta) ){
                double t = (theta - currTheta) / (nextTheta - currTheta);
                double expectedDist = (nextDist - currDist)*t + currDist;
                return dist <= expectedDist;
            }
        }
        return false;
    }

    public static void testConvex() {
        try {
            byte[][][] testImage = ImageUtils.loadImageBytes("red.png");
            List<PolygonPoint> points = new ArrayList();
            for(int ROW = 0; ROW < testImage.length; ROW++){
                for (int COL = 0; COL < testImage[0].length; COL++) {
                    byte[] currPixel = testImage[ROW][COL];
                    int currAlpha = currPixel[0];
                    if(Arrays.equals(currPixel, new byte[]{-1,-1,-1,0}) == false){
                        points.add(new PolygonPoint(COL, ROW));
                    }
                }
            }
            ConvexOrientation hull = new ConvexOrientation(points);
            Log.v(TAG,"points:" + points.size());
            byte[][][] retImage = new byte[testImage.length][testImage[0].length][];
            for(int ROW = 0; ROW < testImage.length; ROW++){
                for (int COL = 0; COL < testImage[0].length; COL++) {
                    byte[] currPixel = ImageUtils.BLACK_ARGB;
                    retImage[ROW][COL] = currPixel;

                }
            }
            for (int i = 0; i < hull.edgePoints.size(); i++) {
                PolygonPoint currPoint = hull.edgePoints.get(i);
                PolygonPoint nextPoint = (i < hull.edgePoints.size() - 1 ? hull.edgePoints.get(i + 1) : hull.edgePoints.get(0));
                for(double t = 0; t <= 1; t +=0.1){
                    int COL = (int)((nextPoint.x - currPoint.x)*t + currPoint.x);
                    int ROW = (int)((nextPoint.y - currPoint.y)*t + currPoint.y);
                    retImage[ROW][COL] = ImageUtils.WHITE_ARGB;
                }
            }
            ImageUtils.saveImageBytes(retImage, "testImage.png");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
