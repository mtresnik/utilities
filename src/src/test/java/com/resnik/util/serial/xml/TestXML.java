package com.resnik.util.serial.xml;

import com.resnik.util.logger.Log;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import static com.resnik.util.serial.xml.XMLTreeParser.fromFileLocation;

public class TestXML {

    public static final String TAG = TestXML.class.getSimpleName();

    @Test
    public void testXML() throws IOException, SAXException, ParserConfigurationException {
        XMLTree tree = fromFileLocation("src/in/test.xml");
        Log.v(TAG,tree);
        Log.v(TAG,tree.findAllByName("sub"));
        Log.v(TAG,tree.findAllByName("root"));
        tree.save("src/res/xml/test.xml");
    }

}
