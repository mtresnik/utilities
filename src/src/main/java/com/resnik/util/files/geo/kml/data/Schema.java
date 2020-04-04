package com.resnik.util.files.geo.kml.data;

import com.resnik.util.files.geo.kml.KMLElement;
import com.resnik.util.files.geo.kml.KMLNode;

public class Schema extends KMLNode {
    public Schema(String name) {
        super(new KMLElement("Schema"));
        this.setName(name);
    }

    public void setName(String name){
        this.getValue().put("name", name);
    }

    public void addField(SimpleField field){
        this.addChild(field);
    }
}