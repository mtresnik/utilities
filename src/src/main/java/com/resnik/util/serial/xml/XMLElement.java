package com.resnik.util.serial.xml;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class XMLElement extends LinkedHashMap<String, Object>{

    String name;
    Node element;
    String inner;

    public XMLElement(Node element){
        super(getMap(element));
        this.name = element.getNodeName();
        this.element = element;
        this.inner = element.getNodeValue();
        if(this.inner == null){
            this.inner = "";
        }
    }

    public XMLElement(String name){
        this(name, "");
    }

    public XMLElement(String name, String inner){
        this.name = name;
        this.inner = inner;
    }

    public static Map<String, Object> getMap(Node element){
        Map<String, Object> retMap = new LinkedHashMap<>();
        NamedNodeMap nodeMap = element.getAttributes();
        if(nodeMap == null){
            return Collections.emptyMap();
        }
        for(int i = 0; i < nodeMap.getLength(); i++){
            Node currNode = nodeMap.item(i);
            String name = currNode.getNodeName();
            String value = currNode.getNodeValue();
            retMap.put(name, value);
        }
        return retMap;
    }

    public static String toObjString(Object t){
//        if(t instanceof String){
//            return "\"" + t + "\"";
//        }
//        return t.toString();
        return "\"" + t + "\"";
    }

    public void setInner(String inner) {
        this.inner = inner;
    }

    public String getInner() {
        return inner;
    }

    public String toString(){
        String retString = "";
        if(this.size() == 0){
           return "";
        }
        int index = 0;
        for(Map.Entry<String, Object> entry : this.entrySet()){
            retString += entry.getKey() + "=" + toObjString(entry.getValue());
            if(index < this.entrySet().size() - 1){
                retString += " ";
            }
            index++;
        }
        return retString;
    }

    public String getName() {
        return name;
    }
}
