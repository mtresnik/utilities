package com.resnik.util.files.kml.style;

public class LabelStyle extends ColorStyle{

    public LabelStyle(){
        super("LabelStyle");
    }

    public void setScale(float scale){
        this.setInner("scale", Float.toString(scale));
    }
}
