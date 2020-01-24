package com.resnik.util.files.kml;

import com.resnik.util.files.kml.feature.Document;
import com.resnik.util.files.kml.feature.Placemark;
import com.resnik.util.files.kml.geometry.Polygon;
import org.junit.Test;

import java.io.FileNotFoundException;

public class TestKML {

    @Test
    public void testKML(){
        KML kml = new KML();
        Document doc = new Document();
        Placemark placemark = new Placemark();
        Polygon circle = Polygon.generateCircle(34, -73, 5);
        placemark.setGeometry(circle);
        doc.addPlacemark(placemark);
        kml.setDocument(doc);
        System.out.println(kml);
        try {
            kml.save("src/res/kml/test.kml");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
