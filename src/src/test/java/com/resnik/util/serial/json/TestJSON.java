package com.resnik.util.serial.json;

import com.resnik.util.serial.xml.XMLTree;
import com.resnik.util.serial.xml.XMLTreeParser;
import com.resnik.util.logger.Log;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.FileReader;
import java.io.IOException;

public class TestJSON {

    public static final String TAG = TestJSON.class.getSimpleName();

    @Test
    public void testParse() throws IOException, SAXException, ParserConfigurationException, ParseException {
        String fileLocation = "src/in/test.json";
        JSONParser parser = new JSONParser();
        Object jsonRoot = parser.parse(new FileReader(fileLocation));
        JSONTree tree = JSONTree.generateFromObject(jsonRoot);
        Log.v(TAG,tree);
        XMLTree xmlTree = tree.toXMLTree();
        xmlTree.save("src/in/xml/from_test_json.xml");
        Log.v(TAG,xmlTree);
        XMLTree loaded = XMLTreeParser.fromFileLocation("src/in/xml/from_test_json.xml");
    }
}
