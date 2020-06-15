package com.resnik.util.serial.geo.kml.style;

public class LabelStyle extends ColorStyle{

    public LabelStyle(){
        super("LabelStyle");
    }

    public void setScale(float scale){
        this.setInner("scale", Float.toString(scale));
    }
}
