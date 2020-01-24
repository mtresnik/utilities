package com.resnik.util.files.kml.region;

import com.resnik.util.files.kml.KMLElement;
import com.resnik.util.files.kml.KMLNode;

public class LatLonBox extends KMLNode {

    public LatLonBox(String name){
        super(new KMLElement(name));
    }

    public LatLonBox() {
        this("LatLonBox");
    }

    public void setNorth(double north){
        this.setInner("north", Double.toString(north));
    }

    public void seSouth(double south){
        this.setInner("south", Double.toString(south));
    }

    public void setEast(double east){
        this.setInner("east", Double.toString(east));
    }

    public void setWest(double west){
        this.setInner("west", Double.toString(west));
    }

}
