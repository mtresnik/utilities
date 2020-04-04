package com.resnik.util.files.geo.kml.region;

import com.resnik.util.files.geo.kml.KMLElement;
import com.resnik.util.files.geo.kml.KMLNode;

public class Region extends KMLNode {
    public Region(KMLElement value) {
        super(value);
    }

    public void setLatLonAltBox(LatLonAltBox latLonAltBox){
        this.replaceChild(latLonAltBox);
    }

    public void setLevelOfDetail(LevelOfDetail levelOfDetail){
        this.replaceChild(levelOfDetail);
    }

}
