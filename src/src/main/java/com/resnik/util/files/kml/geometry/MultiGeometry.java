package com.resnik.util.files.kml.geometry;

import com.resnik.util.files.kml.KMLElement;

public class MultiGeometry extends Geometry {

    public MultiGeometry() {
        super(new KMLElement("MultiGeometry"));
    }

    public void addGeometry(Geometry geometry){
        this.addChild(geometry);
    }

}
