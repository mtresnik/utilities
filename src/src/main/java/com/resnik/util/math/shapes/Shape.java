package com.resnik.util.math.shapes;

import java.util.List;

public interface Shape {

    public boolean contains(PolygonPoint point);

    public List<PolygonPoint> getPoints();

    public default double minX(){
        double ret = Double.MAX_VALUE;
        for(PolygonPoint polygonPoint : getPoints()){
            ret = Math.min(polygonPoint.x, ret);
        }
        return ret;
    }

    public default double minY(){
        double ret = Double.MAX_VALUE;
        for(PolygonPoint polygonPoint : getPoints()){
            ret = Math.min(polygonPoint.y, ret);
        }
        return ret;
    }

    public default double maxX(){
        double ret = -Double.MAX_VALUE;
        for(PolygonPoint polygonPoint : getPoints()){
            ret = Math.max(polygonPoint.x, ret);
        }
        return ret;
    }

    public default double maxY(){
        double ret = -Double.MAX_VALUE;
        for(PolygonPoint polygonPoint : getPoints()){
            ret = Math.max(polygonPoint.y, ret);
        }
        return ret;
    }

    public default BoundingBox toBoundingBox(){
        return new BoundingBox(this.getPoints());
    }

    public default double getArea(){
        return toBoundingBox().getArea();
    }

}
