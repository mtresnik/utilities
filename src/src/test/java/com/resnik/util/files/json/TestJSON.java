package com.resnik.util.files.json;

import com.resnik.util.files.xml.XMLTree;
import com.resnik.util.files.xml.XMLTreeParser;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.FileReader;
import java.io.IOException;

public class TestJSON {


    @Test
    public void testParse() throws IOException, SAXException, ParserConfigurationException, ParseException {
        String fileLocation = "in/test.json";
        JSONParser parser = new JSONParser();
        Object jsonRoot = parser.parse(new FileReader(fileLocation));
        JSONTree tree = JSONTree.generateFromObject(jsonRoot);
        System.out.println(tree);
        XMLTree xmlTree = tree.toXMLTree();
        xmlTree.save("in/xml/from_test_json.xml");
        System.out.println(xmlTree);
        XMLTree loaded = XMLTreeParser.fromFileLocation("in/xml/from_test_json.xml");
    }
}
