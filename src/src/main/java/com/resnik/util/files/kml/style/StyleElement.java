package com.resnik.util.files.kml.style;

import com.resnik.util.files.kml.KMLElement;
import com.resnik.util.files.kml.KMLNode;

public abstract class StyleElement extends KMLNode {

    public StyleElement(String str) {
        super(new KMLElement(str));
    }
}
