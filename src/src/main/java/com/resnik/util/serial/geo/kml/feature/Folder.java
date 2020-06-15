package com.resnik.util.serial.geo.kml.feature;

import com.resnik.util.serial.geo.kml.KMLElement;

public class Folder extends Feature{

    public Folder() {
        super(new KMLElement("Folder"));
    }

    public void addFeature(Feature feature){
        this.addChild(feature);
    }

}
