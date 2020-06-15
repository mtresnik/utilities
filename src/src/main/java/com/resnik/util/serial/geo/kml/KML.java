package com.resnik.util.serial.geo.kml;

import com.resnik.util.serial.FileUtils;
import com.resnik.util.serial.geo.kml.feature.Document;
import com.resnik.util.serial.xml.XMLTree;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class KML extends KMLNode{

    public static final String xmlns = "http://www.opengis.net/kml/2.2";
    public static final String xmlns_atom = "http://www.w3.org/2005/Atom";

    public KML() {
        super(new KMLElement("kml"));
        this.getValue().put("xmlns", xmlns);
        this.getValue().put("xmlns:atom", xmlns_atom);
    }

    public void setDocument(Document document){
        this.replaceChild(document);
    }

    public void save(String fileLocation) throws FileNotFoundException {
        File file = new File(fileLocation);
        if(!file.exists()){
            file.getParentFile().mkdirs();
        }
        String extension = FileUtils.getFileExtension(file);
        if(!extension.equalsIgnoreCase("xml") &&
                !extension.equalsIgnoreCase("txt") &&
                !extension.equalsIgnoreCase("kml") ){
            throw new IllegalArgumentException("Must end in extension type: kml, xml, txt");
        }
        PrintWriter pw = new PrintWriter(file);
        pw.println(XMLTree.XML_TAG);
        pw.println(this.toString());
        pw.close();
    }

}
