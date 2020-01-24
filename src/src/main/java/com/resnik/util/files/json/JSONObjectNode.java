package com.resnik.util.files.json;

import com.resnik.util.files.xml.XMLElement;
import com.resnik.util.files.xml.XMLNode;
import com.resnik.util.objects.structures.tree.TreeNode;

import java.util.Map;

public class JSONObjectNode extends JSONNode<JSONElement> {

    public JSONObjectNode(JSONElement value, TreeNode parent) {
        super(value, parent);
        if(!(value instanceof JSONObjectElement)){
            throw new IllegalArgumentException("Root must be of type JSONObjectElement");
        }
        JSONObjectElement _value = (JSONObjectElement) value;
        Map map = _value.rep;
        for(Object obj : map.entrySet()){
            Map.Entry entry = (Map.Entry) obj;
            JSONElement child = JSONElement.decide(entry.getKey().toString(), entry.getValue());
            this.addChild(JSONNode.decide(child, this));
        }
    }

    public String toString(){
        String retString = "";
        retString += "\"" + this.getValue().name.replaceAll("\"", "") + "\"";
        retString += ":";
        retString += "{";
        JSONObjectElement _value = (JSONObjectElement) this.getValue();
        Map map = _value.rep;
        int count = 0;
        for(Object obj : map.entrySet()){
            Map.Entry entry = (Map.Entry) obj;
            JSONElement child = JSONElement.decide(entry.getKey().toString(), entry.getValue());
            retString += JSONNode.decide(child, this).toString();
            if(count < map.entrySet().size() - 1){
                retString+=",";
            }
            count++;
        }
        retString += "}";
        return retString;
    }

    @Override
    public String getName() {
        return this.getValue().name;
    }

    @Override
    public JSONObjectNode toJSONObjectNode() {
        return this;
    }

    @Override
    public XMLNode toXMLNode() {
        XMLElement xmlElement = new XMLElement(this.getName(), "");
        XMLNode retNode = new XMLNode(xmlElement);
        for(TreeNode child : this.getChildren()){
            if(child instanceof JSONNode){
                JSONNode jsonNode = (JSONNode) child;
                XMLNode newChild = jsonNode.toXMLNode();
                if(newChild != null){
                    retNode.addChild(newChild);
                }
            }
        }
        return retNode;
    }


}
