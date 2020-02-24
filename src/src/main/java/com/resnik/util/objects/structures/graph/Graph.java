package com.resnik.util.objects.structures.graph;

import com.resnik.util.files.xml.XMLElement;
import com.resnik.util.files.xml.XMLElementConvertable;
import com.resnik.util.files.xml.XMLNode;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public class Graph implements XMLElementConvertable {

    public static final String NAME = "graph";
    static{
        XMLElementConvertable.fromXMLNodeMap.put(Graph.class, Graph::fromXMLNode);
    }

    Set<Vertex> vertices;
    Set<Edge> edges;

    public Graph(Set<Vertex> vertices, Set<Edge> edges) {
        this.vertices = vertices;
        this.edges = edges;
    }


    public Graph(){
        this(new LinkedHashSet<>(), new LinkedHashSet<>());
    }

    public boolean addVertex(Vertex vertex){
        return this.vertices.add(vertex);
    }

    public boolean addAllVertices(Collection<Vertex> vertices){
        boolean ret = true;
        for(Vertex vertex : vertices){
            ret &= this.addVertex(vertex);
        }
        return ret;
    }

    public boolean addEdge(Edge edge){
        this.vertices.add(edge.from);
        this.vertices.add(edge.to);
        return this.edges.add(edge);
    }

    public boolean addAllEdges(Collection<Edge> edges){
        boolean ret = true;
        for(Edge edge : edges){
            ret &= this.addEdge(edge);
        }
        return ret;
    }

    public boolean contains(Vertex vertex){
        return vertices.contains(vertex);
    }

    public boolean contains(Edge edge){
        return edges.contains(edge);
    }

    @Override
    public String toString() {
        return "Graph{" +
                "vertices=" + vertices +
                ", edges=" + edges +
                '}';
    }

    @Override
    public XMLElement toXMLElement() {
        return new XMLElement(NAME);
    }

    @Override
    public XMLNode toXMLNode(){
        XMLNode root = new XMLNode(this.toXMLElement());
        XMLNode vertices = XMLElementConvertable.toXMLNodeFromCollection("vertices", this.vertices);
        XMLNode edges = XMLElementConvertable.toXMLNodeFromCollection("edges", this.edges);
        root.addChild(vertices);
        root.addChild(edges);
        return root;
    }

    public static Graph fromXMLNode(XMLNode node){
        if(!node.getName().equals(NAME)) return null;
        Graph retGraph = new Graph();
        for(Object child : node.getChildren()){
            if(!(child instanceof XMLNode)) continue;
            XMLNode _child = (XMLNode) child;
            switch (((XMLNode) child).getName()){
                case "vertices":
                    for(Object grandChild : _child.getChildren()){
                        retGraph.addVertex(Vertex.fromXMLNode((XMLNode) grandChild));
                    }
                    break;
                case "edges":
                    for(Object grandChild : _child.getChildren()){
                        retGraph.addEdge(Edge.fromXMLNode((XMLNode) grandChild));
                    }
                    break;
                default:
                    continue;
            }
        }
        return retGraph;
    }

    public int numVertices(){
        return vertices.size();
    }

    public int numEdges(){
        return edges.size();
    }

    public static Graph testGraph(){
        Vertex a = new Vertex("a");
        Vertex b = new Vertex("b");
        Vertex c = new Vertex("c");
        Vertex d = new Vertex("d");
        Vertex e = new Vertex("e");
        Vertex f = new Vertex("f");
        Vertex g = new Vertex("g");
        Vertex h = new Vertex("h");
        Vertex i = new Vertex("i");
        Graph graph = new Graph();
        graph.addEdge(new Edge(a,e));
        graph.addEdge(new Edge(a,b));
        graph.addEdge(new Edge(e,b));
        graph.addEdge(new Edge(b,f));
        graph.addEdge(new Edge(b,c));
        graph.addEdge(new Edge(f,h));
        graph.addEdge(new Edge(c,h));
        graph.addEdge(new Edge(g,c));
        graph.addEdge(new Edge(c,d));
        graph.addEdge(new Edge(g,d));
        return graph;
    }

    public Vertex getVertex(String name){
        for(Vertex vertex : this.vertices){
            if(Objects.equals(vertex.name, name)){
                return vertex;
            }
        }
        return null;
    }
}
