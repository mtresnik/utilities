package com.resnik.util.serial.geo.kml.geometry;

import com.resnik.util.serial.geo.kml.KMLElement;
import com.resnik.util.serial.geo.kml.transforms.Location;
import com.resnik.util.serial.geo.kml.transforms.Orientation;
import com.resnik.util.serial.geo.kml.transforms.ResourceMap;
import com.resnik.util.serial.geo.kml.transforms.Scale;

public class Model extends Geometry {
    public Model() {
        super(new KMLElement("Model"));
    }

    public void setLocation(Location location){
        this.replaceChild(location);
    }

    public void setOrientation(Orientation orientation){
        this.replaceChild(orientation);
    }

    public void setScale(Scale scale){
        this.replaceChild(scale);
    }

    public void setLink(String link){
        this.setInner("Link", link);
    }

    public void setResourceMap(ResourceMap resourceMap){
        this.replaceChild(resourceMap);
    }

}
