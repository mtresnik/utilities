package com.resnik.util.files.geo.kml.geometry;

import com.resnik.util.files.geo.kml.KMLElement;

public class MultiGeometry extends Geometry {

    public MultiGeometry() {
        super(new KMLElement("MultiGeometry"));
    }

    public void addGeometry(Geometry geometry){
        this.addChild(geometry);
    }

}
