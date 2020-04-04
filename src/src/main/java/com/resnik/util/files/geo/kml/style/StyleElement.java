package com.resnik.util.files.geo.kml.style;

import com.resnik.util.files.geo.kml.KMLElement;
import com.resnik.util.files.geo.kml.KMLNode;

public abstract class StyleElement extends KMLNode {

    public StyleElement(String str) {
        super(new KMLElement(str));
    }
}
