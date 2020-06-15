package com.resnik.util.objects.structures.graph;

import com.resnik.util.serial.xml.XMLElement;
import com.resnik.util.serial.xml.XMLElementConvertable;
import com.resnik.util.serial.xml.XMLNode;
import com.resnik.util.objects.structures.tree.TreeNode;

import java.util.List;
import java.util.Objects;

public class Edge implements XMLElementConvertable {

    public static final String NAME = "edge";
    static{
        XMLElementConvertable.fromXMLNodeMap.put(Edge.class, Edge::fromXMLNode);
    }

    double weight;
    Vertex from, to;
    EdgeDirection direction;

    public Edge(Vertex from, Vertex to, double weight, EdgeDirection direction) {
        this.weight = weight;
        this.from = from;
        this.to = to;
        this.direction = direction;
    }

    public Edge(Vertex from, Vertex to, double weight) {
        this(from, to, weight, EdgeDirection.BIDIRECTIONAL);
    }

    public Edge(Vertex from, Vertex to) {
        this(from, to, 1.0);
    }

    public Edge(Vertex from, Vertex to, EdgeDirection direction) {
        this(from, to, 1.0, direction);
    }

    public boolean containsFrom(Vertex vertex){
        return Objects.equals(from, vertex);
    }

    public boolean isNavigableFrom(Vertex vertex){
        if(this.direction == EdgeDirection.BIDIRECTIONAL){
            return this.contains(vertex);
        }
        return this.containsFrom(vertex);
    }

    public boolean contains(Vertex vertex){
        return Objects.equals(from, vertex) || Objects.equals(to, vertex);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        if(edge.weight != this.weight) return false;
        if(!edge.direction.equals(this.direction)) return false;
        if(this.direction == EdgeDirection.BIDIRECTIONAL){
            if(this.from.equals(edge.from) && this.to.equals(edge.to)){
                return true;
            }
            if(this.to.equals(edge.from) && this.from.equals(edge.to)){
                return true;
            }
        }
        if(this.from.equals(edge.from) && this.to.equals(edge.to)){
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(weight, from, to);
    }

    @Override
    public String toString() {
        return "{" + from + (this.direction == EdgeDirection.BIDIRECTIONAL ? "<" : "") + "--" + this.weight + "-->" + this.to + "}";
    }

    @Override
    public XMLElement toXMLElement(){
        XMLElement retElement = new XMLElement(NAME);
        retElement.put("weight", weight);
        retElement.put("direction", direction);
        return retElement;
    }

    @Override
    public XMLNode toXMLNode(){
        XMLNode retNode = new XMLNode(this.toXMLElement());
        XMLNode from = new XMLNode(new XMLElement("from"));
        from.addChild(this.from.toXMLNode());
        XMLNode to = new XMLNode(new XMLElement("to"));
        to.addChild(this.to.toXMLNode());
        retNode.addChild(from);
        retNode.addChild(to);
        return retNode;
    }

    public static Edge fromXMLNode(XMLNode element){
        if(!element.getName().equals(NAME)){
            return null;
        }
        double weight = Double.parseDouble(((XMLElement)element.getValue()).getOrDefault("weight", "1.0").toString());
        String dString = ((XMLElement)element.getValue()).getOrDefault("direction", EdgeDirection.BIDIRECTIONAL.toString()).toString();
        EdgeDirection direction = EdgeDirection.valueOf(dString);
        List<TreeNode<XMLElement>> children = element.getChildren();
        Vertex from = null;
        Vertex to = null;
        for(TreeNode<XMLElement> child : children){
            if(!(child instanceof XMLNode)) continue;
            XMLNode childNode = (XMLNode) child;
            switch (childNode.getName()){
                case "from":
                    from = Vertex.fromXMLNode((XMLNode) childNode.getChildren().get(0));
                    break;
                case "to":
                    to = Vertex.fromXMLNode((XMLNode) childNode.getChildren().get(0));
                    break;
                default:
                    continue;
            }
        }
        Edge retEdge = new Edge(from, to, weight, direction);
        return retEdge;
    }

    public Vertex to(Vertex from){
        if(from.equals(this.from)){
            return to;
        }
        if(this.direction == EdgeDirection.BIDIRECTIONAL){
            return this.from;
        }
        return null;
    }

    public Vertex from(Vertex to){
        if(to.equals(this.to)){
            return from;
        }
        if(this.direction == EdgeDirection.BIDIRECTIONAL){
            return this.to;
        }
        return null;
    }

    public double getWeight() {
        return weight;
    }
}
