package com.resnik.util.files.json;

import com.resnik.util.files.xml.XMLNode;
import com.resnik.util.objects.structures.tree.TreeNode;

public abstract class JSONNode<T> extends TreeNode<T> {

    public JSONNode(T value, TreeNode<T> parent) {
        super(value, parent);
    }

    public static JSONNode decide(JSONElement element, JSONNode parent){
        if(element instanceof JSONArrayElement){
            return new JSONArrayNode(element, parent);
        }
        if(element instanceof JSONObjectElement){
            return new JSONObjectNode(element, parent);
        }
        return new JSONValueNode(element.name, element.rep, parent);
    }

    public abstract String getName();

    public abstract JSONObjectNode toJSONObjectNode();

    public abstract XMLNode toXMLNode();

}
