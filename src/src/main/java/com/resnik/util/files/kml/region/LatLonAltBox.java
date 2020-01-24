package com.resnik.util.files.kml.region;

import com.resnik.util.files.kml.geometry.Geometry;

public class LatLonAltBox extends LatLonBox {

    public LatLonAltBox() {
        super("LatLonAltBox");
    }

    public void setMaxAltitude(float maxAltitude){
        this.setInner("maxAltitude", Float.toString(maxAltitude));
    }

    public void setMinAltitude(float minAltitude){
        this.setInner("minAltitude", Float.toString(minAltitude));
    }

    private void clearAltitudeMode(){
        this.removeChildByTag("altitude");
        this.removeChildByTag("gx:altitudeMode");
    }

    public void setAltitudeMode(Geometry.AltitudeMode altitudeMode){
        this.clearAltitudeMode();
        if(altitudeMode == Geometry.AltitudeMode.clampToSeaFloor || altitudeMode == Geometry.AltitudeMode.relativeToSeaFloor){
            this.setInner("gx:altitudeMode", altitudeMode.toString());
        }else{
            this.setInner("altitudeMode", altitudeMode.toString());
        }
    }

}
