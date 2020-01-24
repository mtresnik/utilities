package com.resnik.util.files.kml.style;

public class PolyStyle extends ColorStyle {

    public PolyStyle(int id) {
        super("PolyStyle");
        this.setInner("id", Integer.toString(id));
    }

    public void setFill(boolean fill){
        this.setInner("fill", Integer.toString(fill ? 1 : 0));
    }

    public void setOutline(boolean outline){
        this.setInner("outline", Integer.toString(outline ? 1 : 0));
    }


}
