package com.resnik.util.objects.structures.graph;

import com.resnik.util.files.xml.XMLTree;
import com.resnik.util.files.xml.XMLTreeParser;
import com.resnik.util.objects.structures.graph.traversals.BFSTraversal;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.FileNotFoundException;
import java.io.IOException;

public class TestGraph {

    @Test
    public void testTraversal() throws FileNotFoundException {
        Graph graph = Graph.testGraph();
        Vertex a = graph.getVertex("a");
        Vertex d = graph.getVertex("d");
        Vertex g = graph.getVertex("g");
        Vertex i = graph.getVertex("i");
        BFSTraversal bfsTraversal = new BFSTraversal();
        Path ad = bfsTraversal.getPath(a,d, graph);
        System.out.println(ad);;
        ad.toXMLTree().save("res/graph/bfs_path.xml");
        System.out.println(bfsTraversal.getPath(g,i, graph));;
        graph.toXMLTree().save("res/graph/bfs_graph.xml");
    }

    @Test
    public void testLoadSave() throws IOException, SAXException, ParserConfigurationException {
        XMLTree tree = XMLTreeParser.fromFileLocation("in/graph/test1.xml");
        Graph g1 = Graph.fromXMLNode(tree.getRoot());
        g1.toXMLTree().save("in/graph/test2.xml");
        System.out.println(g1);
    }

    @Test
    public void testLoadSavePath() throws IOException, SAXException, ParserConfigurationException {
        Vertex[] vertices = new Vertex[10];
        for(int i = 0; i < vertices.length; i++){
            vertices[i] = new Vertex();
        }
        Path path = new Path();
        path.add(vertices[0]);
        path.add(vertices[1]);
        System.out.println(path);
        System.out.println(path.toXMLNode());
        path.toXMLTree().save("in/graph/test_path.xml");
        Path loadPath = Path.fromXMLNode(XMLTreeParser.fromFileLocation("in/graph/test_path.xml").getRoot());
        System.out.println(loadPath);
    }

}
