package com.resnik.util.files.json;

import com.resnik.util.files.xml.XMLElement;
import com.resnik.util.files.xml.XMLNode;
import com.resnik.util.objects.structures.tree.TreeNode;

public class JSONValueNode extends JSONNode<Object> {

    String name;

    public JSONValueNode(String name, Object value, TreeNode parent) {
        super(value, parent);
        this.name = name;
    }

    public String toString(){
        return this.getName() + ":" + (this.getValue() instanceof String ? "\"" + this.getValue() + "\"" : this.getValue());
    }

    @Override
    public String getName() {
        return "\"" + (name == null ? "value" : name).replaceAll("\"", "") + "\"";
    }

    @Override
    public JSONObjectNode toJSONObjectNode() {
        return new JSONObjectNode(new JSONValueElement(name, this.getValue()).toJSONObjectElement(), this.getParent());
    }

    @Override
    public XMLNode toXMLNode() {
        XMLElement xmlElement = new XMLElement(this.name, this.getValue().toString());
        return new XMLNode(xmlElement);
    }
}
