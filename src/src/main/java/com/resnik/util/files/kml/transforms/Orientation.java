package com.resnik.util.files.kml.transforms;

import com.resnik.util.files.kml.KMLElement;
import com.resnik.util.files.kml.KMLNode;

public class Orientation extends KMLNode {
    public Orientation() {
        super(new KMLElement("Orientation"));
    }

    public void setHeading(double heading){
        this.setInner("heading", Double.toString(heading));
    }

    public void setTilt(double tilt){
        this.setInner("tilt", Double.toString(tilt));
    }

    public void setRoll(double roll){
        this.setInner("roll", Double.toString(roll));
    }

}
