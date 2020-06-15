package com.resnik.util.serial.geo.kml.geometry;

import com.resnik.util.serial.geo.kml.KMLElement;
import com.resnik.util.serial.geo.kml.KMLNode;
import com.resnik.util.geo.GeoUtils;

public class Polygon extends Geometry{

    private Double minLon, maxLon, minLat, maxLat;

    public Polygon() {
        super(new KMLElement("Polygon"));
    }

    public static class OuterBoundary extends KMLNode {

        public OuterBoundary() {
            super(new KMLElement("outerBoundaryIs"));
        }

        public void setLinearRing(LinearRing linearRing){
            this.replaceChild(linearRing);
        }
    }

    public static class InnerBoundary extends KMLNode{

        public InnerBoundary(){super((new KMLElement("innerBoundaryIs")));}

        public void setLinearRing(LinearRing linearRing){
            this.replaceChild(linearRing);
        }

    }

    public void setOuterBoundary(OuterBoundary outerBoundary){
        this.replaceChild(outerBoundary);
    }

    public void setOuterBoundary(LinearRing linearRing){
        OuterBoundary outerBoundary = new OuterBoundary();
        outerBoundary.setLinearRing(linearRing);
        this.setOuterBoundary(outerBoundary);
    }

    public void setInnerBoundary(LinearRing linearRing){
        InnerBoundary innerBoundary = new InnerBoundary();
        innerBoundary.setLinearRing(linearRing);
        this.setInnerBoundary(innerBoundary);
    }

    public void setInnerBoundary(InnerBoundary innerBoundary){
        this.replaceChild(innerBoundary);
    }

    public static double[] boundingBox(double centerLat, double centerLon, double radiusMeters){
        double dLat = 1e-4;
        double dLon = 1e-4;
        double maxLat = centerLat;
        while(GeoUtils.haversine(centerLat, centerLon, maxLat, centerLon) < radiusMeters){
            maxLat += dLat;
        }
        double maxLon = centerLon;
        while(GeoUtils.haversine(centerLat, centerLon, centerLat, maxLon) < radiusMeters){
            maxLon += dLon;
        }
        double minLon = centerLon - (maxLon - centerLon);
        double minLat = centerLat - (maxLat - centerLat);
        return new double[]{minLat, minLon, maxLat, maxLon};
    }

    public static Polygon generateCircle(double centerLat, double centerLon, double radiusKM){
        final int SIZE = (int) Math.max(1000*radiusKM, 1000);
        double[] circlex = new double[SIZE];
        double[] circley = new double[SIZE];
        double dLat = 1e-5;
        double dLon = 1e-5;
        double radiusMeters = radiusKM*1000.0;
        double maxLat = centerLat;
        while(GeoUtils.haversine(centerLat, centerLon, maxLat, centerLon) < radiusMeters){
            maxLat += dLat;
        }
        double maxLon = centerLon;
        while(GeoUtils.haversine(centerLat, centerLon, centerLat, maxLon) < radiusMeters){
            maxLon += dLon;
        }
        double rLon = Math.abs(maxLon - centerLon);
        double rLat = Math.abs(maxLat - centerLat);
        double minLon = Double.MAX_VALUE;
        double minLat = Double.MAX_VALUE;
        double dTheta = 2*Math.PI / SIZE;
        for(int index = 0; index < SIZE; index++){
            double currTheta = index*dTheta;
            double currX = rLon*Math.cos(currTheta) + centerLon;
            double currY = rLat*Math.sin(currTheta) + centerLat;
            minLon = Math.min(minLon, currX);
            minLat = Math.min(minLat, currY);
            circlex[index] = currX;
            circley[index] = currY;
        }
        LinearRing ring = new LinearRing(circlex, circley);
        OuterBoundary outerBoundary = new OuterBoundary();
        outerBoundary.setLinearRing(ring);
        Polygon retPoly = new Polygon();
        retPoly.setOuterBoundary(outerBoundary);

        retPoly.minLat = minLat;
        retPoly.maxLat = maxLat;

        retPoly.minLon = minLon;
        retPoly.maxLon = maxLon;
        return retPoly;
    }

    public double getMinLon() {
        return minLon;
    }

    public double getMaxLon() {
        return maxLon;
    }

    public double getMinLat() {
        return minLat;
    }

    public double getMaxLat() {
        return maxLat;
    }
}
