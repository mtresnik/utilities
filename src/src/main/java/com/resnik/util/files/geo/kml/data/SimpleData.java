package com.resnik.util.files.geo.kml.data;

import com.resnik.util.files.geo.kml.KMLElement;
import com.resnik.util.files.geo.kml.KMLNode;

public class SimpleData extends KMLNode {

    public SimpleData() {
        super(new KMLElement("SimpleData"));
    }

    public void setName(String name){
        this.getValue().put("name", name);
    }

    public void setInner(String inner){
        this.getValue().setInner(inner);
    }

}
