package com.resnik.util.files.kml.data;

import com.resnik.util.files.kml.KMLElement;
import com.resnik.util.files.kml.KMLNode;

public class Data extends KMLNode {

    public Data(String displayName, String value) {
        super(new KMLElement("Data"));
        this.setDisplayName(displayName);
        this.setValue(value);
    }

    public void setDisplayName(String displayName){
        this.setInner("displayName", displayName);
    }

    public void setValue(String value){
        this.setInner("value", value);
    }

}
