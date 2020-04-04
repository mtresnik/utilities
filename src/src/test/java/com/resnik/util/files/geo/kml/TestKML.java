package com.resnik.util.files.geo.kml;

import com.resnik.util.files.geo.kml.feature.Document;
import com.resnik.util.files.geo.kml.feature.Placemark;
import com.resnik.util.files.geo.kml.geometry.Polygon;
import com.resnik.util.logger.Log;
import org.junit.Test;

import java.io.FileNotFoundException;

public class TestKML {

    public static final String TAG = TestKML.class.getSimpleName();

    @Test
    public void testKML(){
        KML kml = new KML();
        Document doc = new Document();
        Placemark placemark = new Placemark();
        Polygon circle = Polygon.generateCircle(34, -73, 5);
        placemark.setGeometry(circle);
        doc.addPlacemark(placemark);
        kml.setDocument(doc);
        Log.v(TAG,kml);
        try {
            kml.save("src/res/kml/test.kml");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
