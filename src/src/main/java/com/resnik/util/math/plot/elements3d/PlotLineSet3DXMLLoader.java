package com.resnik.util.math.plot.elements3d;

import com.resnik.util.files.xml.*;
import com.resnik.util.math.plot.points.Point3d;
import com.resnik.util.objects.structures.graph.Graph;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class PlotLineSet3DXMLLoader implements XMLElementConvertable {

    static{
        XMLElementConvertable.fromXMLNodeMap.put(PlotLineSet3D.class, PlotLineSet3D::fromXMLNode);
    }

    public static PlotLineSet3D load(String fileLocation) throws IOException, ParserConfigurationException, SAXException {
        XMLTree tree = XMLTreeParser.fromFileLocation(fileLocation);
        PlotLineSet3D plotLineset = PlotLineSet3D.fromXMLNode(tree.getRoot());
        return plotLineset;
    }

    @Override
    public XMLElement toXMLElement() {
        return null;
    }

}
