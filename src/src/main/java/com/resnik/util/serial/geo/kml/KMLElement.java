package com.resnik.util.serial.geo.kml;

import com.resnik.util.serial.xml.XMLElement;
import org.w3c.dom.Node;

public class KMLElement extends XMLElement {

    public KMLElement(Node element) {
        super(element);
    }

    public KMLElement(String name) {
        super(name);
    }

    public KMLElement(String name, String inner) {
        super(name, inner);
    }

}
