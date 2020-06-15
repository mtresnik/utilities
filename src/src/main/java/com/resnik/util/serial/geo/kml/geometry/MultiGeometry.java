package com.resnik.util.serial.geo.kml.geometry;

import com.resnik.util.serial.geo.kml.KMLElement;

public class MultiGeometry extends Geometry {

    public MultiGeometry() {
        super(new KMLElement("MultiGeometry"));
    }

    public void addGeometry(Geometry geometry){
        this.addChild(geometry);
    }

}
