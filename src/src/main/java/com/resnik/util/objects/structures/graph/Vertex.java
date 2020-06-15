package com.resnik.util.objects.structures.graph;

import com.resnik.util.serial.xml.XMLElement;
import com.resnik.util.serial.xml.XMLElementConvertable;
import com.resnik.util.serial.xml.XMLNode;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class Vertex implements XMLElementConvertable {

    public static final String NAME = "vertex";
    static{
        XMLElementConvertable.fromXMLNodeMap.put(Vertex.class, Vertex::fromXMLNode);
    }

    String name;
    private static final Map<String, Vertex> allCreated = new LinkedHashMap<>();

    public Vertex(String name) {
        this.name = name;
        allCreated.put(name, this);
    }

    public static Vertex getOrCreate(String name){
        if(allCreated.containsKey(name)){
            return allCreated.get(name);
        }
        Vertex ret = new Vertex(name);
        return ret;
    }

    public Vertex(){
        this("v" +allCreated.size());
    }

    public Set<Edge> getConnectedEdges(Graph graph){
        Set<Edge> retSet = new LinkedHashSet<>();
        for(Edge edge : graph.edges){
            if(edge.isNavigableFrom(this)){
                retSet.add(edge);
            }
        }
        return retSet;
    }

    @Override
    public boolean equals(Object other){
        if(this == other) return true;
        if(!(other instanceof Vertex)) return false;
        if(!(name.equals(((Vertex) other).name))) return false;
        return true;
    }

    @Override
    public String toString(){
        return this.name;
    }

    @Override
    public XMLElement toXMLElement(){
        XMLElement xmlVertex = new XMLElement(NAME);
        xmlVertex.put("name", this.name.toString());
        return xmlVertex;
    }

    public static Vertex fromXMLNode(XMLNode node){
        if(!node.getName().equals(NAME)) return null;
        return getOrCreate(((XMLElement)node.getValue()).get("name").toString());
    }

}
