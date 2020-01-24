package com.resnik.util.files.xml;

import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import static com.resnik.util.files.xml.XMLTreeParser.fromFileLocation;

public class TestXML {

    @Test
    public void testXML() throws IOException, SAXException, ParserConfigurationException {
        XMLTree tree = fromFileLocation("in/test.xml");
        System.out.println(tree);
        System.out.println(tree.findAllByName("sub"));
        System.out.println(tree.findAllByName("root"));
        tree.save("out/xml/test.xml");
    }

}
