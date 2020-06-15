package com.resnik.util.serial.geo.kml.style;

import com.resnik.util.serial.geo.kml.KMLElement;
import com.resnik.util.serial.geo.kml.KMLNode;

public abstract class StyleElement extends KMLNode {

    public StyleElement(String str) {
        super(new KMLElement(str));
    }
}
