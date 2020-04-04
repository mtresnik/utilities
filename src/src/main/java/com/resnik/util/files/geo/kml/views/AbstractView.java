package com.resnik.util.files.geo.kml.views;

import com.resnik.util.files.geo.kml.KMLElement;
import com.resnik.util.files.geo.kml.KMLNode;

public abstract class AbstractView extends KMLNode {
    public AbstractView(KMLElement value) {
        super(value);
    }

    public void setLongitude(float longitude){
        this.setInner("longitude", Float.toString(longitude));
    }

    public void setLatitude(float latitude){
        this.setInner("latitude", Float.toString(latitude));
    }

    public void setAltitude(float altitude){
        this.setInner("altitude", Float.toString(altitude));
    }

    public void setTilt(double tilt){
        this.setInner("tilt", Double.toString(tilt));
    }

    public void setHeading(double heading){
        this.setInner("heading", Double.toString(heading));
    }

}
