package com.resnik.util.files.kml.transforms;

import com.resnik.util.files.kml.KMLElement;
import com.resnik.util.files.kml.KMLNode;

public class ResourceMap extends KMLNode {

    public ResourceMap() {
        super(new KMLElement("ResourceMap"));
    }

    public void addAlias(Alias alias){
        this.addChild(alias);
    }

}
