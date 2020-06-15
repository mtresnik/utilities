package com.resnik.util.objects.structures.graph;

import com.resnik.util.serial.xml.XMLElement;
import com.resnik.util.serial.xml.XMLElementConvertable;
import com.resnik.util.serial.xml.XMLNode;

import java.util.LinkedHashSet;
import java.util.Set;

public class Path extends LinkedHashSet<Vertex> implements XMLElementConvertable {

    public static final String NAME = "path";

    public double weight;

    public Path(){
        super();
    }

    public Path(Set<Vertex> vertices){
        this(vertices, 0.0);
    }

    public Path(Set<Vertex> vertices, double weight){
        super(vertices);
        this.weight = weight;
    }

    @Override
    public XMLElement toXMLElement() {
        XMLElement retElement = new XMLElement(NAME);
        retElement.put("weight", weight);
        return retElement;
    }

    @Override
    public XMLNode toXMLNode(){
        XMLNode retNode = new XMLNode(this.toXMLElement());
        for(Vertex vertex : this){
            retNode.addChild(vertex.toXMLNode());
        }
        return retNode;
    }

    public static Path fromXMLNode(XMLNode node){
        if(!node.getName().equals(NAME)) return null;
        double weight = Double.parseDouble(((XMLElement)node.getValue()).getOrDefault("weight", "0.0").toString());
        Path retPath = new Path();
        for(Object child : node.getChildren()){
            if(!(child instanceof XMLNode)) continue;
            switch (((XMLNode) child).getName()){
                case Vertex.NAME:
                    retPath.add(Vertex.fromXMLNode((XMLNode) child));
                    break;
                default:
                    continue;
            }
        }
        retPath.weight = weight;
        return retPath;
    }


    public String toString(){
        return "Path:[" + weight + "]" + super.toString();
    }

}
