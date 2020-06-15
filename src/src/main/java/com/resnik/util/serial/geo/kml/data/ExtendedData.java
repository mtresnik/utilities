package com.resnik.util.serial.geo.kml.data;

import com.resnik.util.serial.geo.kml.KMLElement;
import com.resnik.util.serial.geo.kml.KMLNode;

public class ExtendedData extends KMLNode {

    public ExtendedData() {
        super(new KMLElement("ExtendedData"));
    }

    public void addSchemaData(SchemaData data){
        this.addChild(data);
    }

    public void addData(Data data){
        this.addChild(data);
    }



}
