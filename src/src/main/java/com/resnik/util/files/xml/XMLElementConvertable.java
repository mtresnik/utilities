package com.resnik.util.files.xml;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public interface XMLElementConvertable {


    public XMLElement toXMLElement();

    public default XMLNode toXMLNode(){
        return new XMLNode(this.toXMLElement());
    }

    public default XMLTree toXMLTree(){
        return new XMLTree(this.toXMLNode());
    }

    public static XMLNode toXMLNodeFromCollection(String name, Collection inputCollection){
        XMLElement element = new XMLElement(name);
        XMLNode retNode = new XMLNode(element);
        for(Object obj : inputCollection){
            if(!(obj instanceof XMLElementConvertable)) continue;
            XMLElementConvertable convertable = (XMLElementConvertable) obj;
            retNode.addChild(convertable.toXMLNode());
        }
        return retNode;
    }

    public static Map<Class, Function<XMLNode, Object>> fromXMLNodeMap = new LinkedHashMap<>();

}
