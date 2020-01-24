package com.resnik.util.files.kml.geometry;

import com.resnik.util.files.kml.KMLElement;

public class GXTrack extends Geometry {
    public GXTrack() {
        super(new KMLElement("gx:Track"));
    }

    public void setWhen(String when){
        this.setInner("when", when);
    }

    public void setCoords(String coords){
        this.setInner("gx:coord", coords);
    }

    public void setAngles(String angles){
        this.setInner("gx:angles", angles);
    }

}
