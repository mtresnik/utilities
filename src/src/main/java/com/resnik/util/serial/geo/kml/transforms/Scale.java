package com.resnik.util.serial.geo.kml.transforms;

import com.resnik.util.serial.geo.kml.KMLElement;
import com.resnik.util.serial.geo.kml.KMLNode;

public class Scale extends KMLNode {

    public Scale() {
        super(new KMLElement("Scale"));
    }

    public void setX(double x){
        this.setInner("x", Double.toString(x));
    }

    public void setY(double y){
        this.setInner("y", Double.toString(y));
    }

    public void setZ(double z){
        this.setInner("z", Double.toString(z));
    }

}
