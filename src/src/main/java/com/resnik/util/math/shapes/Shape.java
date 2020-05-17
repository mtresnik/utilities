package com.resnik.util.math.shapes;

import java.util.List;

public interface Shape {

    boolean contains(PolygonPoint point);

    List<PolygonPoint> getPoints();

    default double minX(){
        double ret = Double.MAX_VALUE;
        for(PolygonPoint polygonPoint : getPoints()){
            ret = Math.min(polygonPoint.x, ret);
        }
        return ret;
    }

    default double minY(){
        double ret = Double.MAX_VALUE;
        for(PolygonPoint polygonPoint : getPoints()){
            ret = Math.min(polygonPoint.y, ret);
        }
        return ret;
    }

    default double maxX(){
        double ret = -Double.MAX_VALUE;
        for(PolygonPoint polygonPoint : getPoints()){
            ret = Math.max(polygonPoint.x, ret);
        }
        return ret;
    }

    default double maxY(){
        double ret = -Double.MAX_VALUE;
        for(PolygonPoint polygonPoint : getPoints()){
            ret = Math.max(polygonPoint.y, ret);
        }
        return ret;
    }

    default BoundingBox toBoundingBox(){
        return new BoundingBox(this.getPoints());
    }

    default double getArea(){
        return toBoundingBox().getArea();
    }

    default PolygonPoint getCenter(){
        BoundingBox boundingBox = this.toBoundingBox();
        return boundingBox.getCenter();
    }

}
