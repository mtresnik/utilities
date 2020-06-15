package com.resnik.util.serial.geo.kml.transforms;

import com.resnik.util.serial.geo.kml.KMLElement;
import com.resnik.util.serial.geo.kml.KMLNode;

public class Alias extends KMLNode {

    public Alias() {
        super(new KMLElement("Alias"));
    }

    public void setTargetHref(String targetHref){
        this.setInner("targetHref", targetHref);
    }

    public void setSourceHref(String sourceHref){
        this.setInner("sourceHref", sourceHref);
    }

}
