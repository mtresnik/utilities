package com.resnik.util.serial.geo.kml.transforms;

import com.resnik.util.serial.geo.kml.KMLElement;
import com.resnik.util.serial.geo.kml.KMLNode;

public class Location extends KMLNode {
    public Location() {
        super(new KMLElement("Location"));
    }

    public void setLongitude(double longitude){
        this.setInner("longitude", Double.toString(longitude));
    }

    public void setLatitude(double latitude){
        this.setInner("latitude", Double.toString(latitude));
    }

    public void setAltitude(double altitude){
        this.setInner("altitude", Double.toString(altitude));
    }
}
