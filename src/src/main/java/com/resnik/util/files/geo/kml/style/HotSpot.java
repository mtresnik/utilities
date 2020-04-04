package com.resnik.util.files.geo.kml.style;

public class HotSpot extends StyleElement{

    public HotSpot() {
        super("hotSpot");
    }

    public void setX(float x){
        this.setInner("x", Float.toString(x));
    }

    public void setY(float y){
        this.setInner("y", Float.toString(y));
    }

    public void setXUnits(String xUnits){
        this.setInner("xunits", xUnits);
    }

    public void setYUnits(String yUnits){
        this.setInner("yunits", yUnits);
    }
}
