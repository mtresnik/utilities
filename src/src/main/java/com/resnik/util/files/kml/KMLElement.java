package com.resnik.util.files.kml;

import com.resnik.util.files.xml.XMLElement;
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
