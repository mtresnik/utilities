package com.resnik.util.files.kml.style;

import com.resnik.util.files.kml.KMLElement;
import com.resnik.util.files.kml.KMLNode;

public class Pair extends KMLNode {
    public Pair() {
        super(new KMLElement("Pair"));
    }

    private void clearStyles(){
        this.removeChildByTag("styleUrl");
        this.removeChildByTag("Style");
    }

    public void setStyleUrl(StyleStateEnum styleStateEnum){
        clearStyles();
        this.setInner("styleUrl", styleStateEnum.toString());
    }

    public void setStyle(Style style){
        this.clearStyles();
        this.replaceChild(style);
    }
}
