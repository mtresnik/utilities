package com.resnik.util.serial.xml;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;

public class XMLTreeParser {

    public static XMLTree fromFileLocation(String fileLocation) throws ParserConfigurationException, IOException, SAXException {
        return fromInputStream(new FileInputStream(new File(fileLocation)));
    }

    public static XMLTree fromString(String inputString) throws ParserConfigurationException, IOException, SAXException {
        return fromInputStream(new StringBufferInputStream(inputString));
    }

    public static XMLTree fromInputStream(InputStream stream) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory builderFactory =  DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        Document doc = builder.parse(stream);
        doc.getDocumentElement().normalize();
        XMLTree retTree = new XMLTree(doc.getDocumentElement());
        return retTree;
    }

}
