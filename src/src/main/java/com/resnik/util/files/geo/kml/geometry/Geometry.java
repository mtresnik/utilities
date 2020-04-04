package com.resnik.util.files.geo.kml.geometry;

import com.resnik.util.files.geo.kml.KMLElement;
import com.resnik.util.files.geo.kml.KMLNode;

public abstract class Geometry extends KMLNode {

    public Geometry(KMLElement value) {
        super(value);
    }

    public enum AltitudeMode{
        clampToGround, relativeToGround, absolute,
        clampToSeaFloor, relativeToSeaFloor
    }

    public void setExtrude(boolean value){
        this.setInner("extrude", Integer.toString(value ? 1 : 0));
    }

    private void clearAltitudeMode(){
        this.removeChildByTag("altitude");
        this.removeChildByTag("gx:altitudeMode");
    }

    public void setAltitudeMode(AltitudeMode altitudeMode){
        this.clearAltitudeMode();
        if(altitudeMode == AltitudeMode.clampToSeaFloor || altitudeMode == AltitudeMode.relativeToSeaFloor){
            this.setInner("gx:altitudeMode", altitudeMode.toString());
        }else{
            this.setInner("altitudeMode", altitudeMode.toString());
        }
    }

    public void setAltitiudeOffset(double offset){
        this.setInner("gx:altitudeOffset", Double.toString(offset));
    }

}
