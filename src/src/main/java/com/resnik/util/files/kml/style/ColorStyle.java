package com.resnik.util.files.kml.style;


public class ColorStyle extends StyleElement{

    public ColorStyle() {
        this("ColorStyle");
    }

    public ColorStyle(String name){
        super(name);
    }

    public enum colorMode{
        normal, random
    }

    public void setColor(String color){
        this.setInner("color", color);
    }

    public void setColorMode(colorMode colorMode){
        this.setInner("colorMode", colorMode.toString());
    }

}
