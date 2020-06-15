package com.resnik.util.serial.geo.kml.views;

import com.resnik.util.serial.geo.kml.KMLElement;

public class LookAt extends AbstractView {
    public LookAt() {
        super(new KMLElement("LookAt"));
    }

    public void setRange(double range){
        this.setInner("range", Double.toString(range));
    }


}
