package com.resnik.util.files.kml.feature;

import com.resnik.util.files.kml.KMLElement;
import com.resnik.util.files.kml.style.Style;

public class Document extends Feature {

    public Document() {
        super(new KMLElement("Document"));
    }

    public void addStyle(Style style){
        this.addChild(style);
    }

    public void addPlacemark(Placemark placemark){
        this.addChild(placemark);
    }

    public void addFolder(Folder folder){
        this.addFolder(folder);
    }

    public static void main(String[] args) {
        Document doc = new Document();
        doc.setName("yolo");
        doc.open();
        doc.setDescription("test description");
        doc.close();
        System.out.println(doc.toString());
    }

}
