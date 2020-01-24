package com.resnik.util.files.kml.data;

import com.resnik.util.files.kml.KMLElement;
import com.resnik.util.files.kml.KMLNode;

public class SimpleField extends KMLNode {

    public SimpleField(SimpleFieldType type, String name) {
        super(new KMLElement("SimpleField"));
    }

    public void setFieldType(SimpleFieldType type){
        this.getValue().put("type", type.toString());
    }

    public void setName(String name){
        this.getValue().put("name", name);
    }

    public void setDisplayName(String displayName){
        this.setInner("displayName", displayName);
    }

}
