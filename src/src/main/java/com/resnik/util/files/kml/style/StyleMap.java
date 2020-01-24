package com.resnik.util.files.kml.style;

import com.resnik.util.files.kml.KMLElement;

public class StyleMap extends StyleSelector {

    public StyleMap(Pair pair) {
        super(new KMLElement("StyleMap"));
        this.addPair(pair);
    }

    public void addPair(Pair pair){
        this.addChild(pair);
    }
}
