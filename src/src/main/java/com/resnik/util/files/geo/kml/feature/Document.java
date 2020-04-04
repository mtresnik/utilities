package com.resnik.util.files.geo.kml.feature;

import com.resnik.util.files.geo.kml.KMLElement;
import com.resnik.util.files.geo.kml.style.Style;
import com.resnik.util.logger.Log;

public class Document extends Feature {

    public static final String TAG = Document.class.getSimpleName();

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
        Log.v(TAG,doc.toString());
    }

}
