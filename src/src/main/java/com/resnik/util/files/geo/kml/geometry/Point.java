package com.resnik.util.files.geo.kml.geometry;

import com.resnik.util.files.geo.kml.KMLElement;

public class Point extends Geometry {
    public Point() {
        super(new KMLElement("Point"));
    }

    public void setCoordinates(float lon, float lat, float ... alt){
        while(lon < -180){
            lon += 360;
        }
        while(lon > 180){
            lon -= 360;
        }
        while(lat < -90){
            lat += 180;
        }
        while(lat > 90){
            lat -= 180;
        }
        String coordString = lon + "," + lat;
        float append = alt.length > 0 ? alt[0] : 0.0f;
        coordString += "," + append;
        this.setInner("coordinates", coordString);
    }

}
