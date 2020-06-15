package com.resnik.util.serial.geo.kml.data;

import com.resnik.util.serial.geo.kml.KMLElement;
import com.resnik.util.serial.geo.kml.KMLNode;

public class SchemaData extends KMLNode {

    public SchemaData(String schemaUrl) {
        super(new KMLElement("SchemaData"));
        this.setSchemaUrl(schemaUrl);
    }

    public void addData(SimpleData data){
        this.addChild(data);
    }

    public void setSchemaUrl(String schemaUrl){
        this.getValue().put("schemaUrl", schemaUrl);
    }

}
