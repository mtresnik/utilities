package com.resnik.util.files.kml.geometry;

import com.resnik.util.files.kml.KMLElement;

public class GXMultiTrack extends Geometry {

    public GXMultiTrack(GXTrack track) {
        super(new KMLElement("gx:MultiTrack"));
        this.addChild(track);
    }

    public void addTrack(GXTrack track){
        this.addChild(track);
    }

}
