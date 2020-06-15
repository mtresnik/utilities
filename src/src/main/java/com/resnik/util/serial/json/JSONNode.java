package com.resnik.util.serial.json;

import com.resnik.util.serial.xml.XMLNode;
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

    public JSONNode<T> findChildByName(String name){
        for(TreeNode<T> child : this.getChildren()){
            if(child instanceof JSONNode){
                if(((JSONNode<T>) child).getName().equals(name)){
                    return (JSONNode<T>)child;
                }
            }
        }
        for(TreeNode<T> child : this.getChildren()){
            if(child instanceof JSONNode && child.hasChildren()){
                JSONNode<T> found = ((JSONNode<T>) child).findChildByName(name);
                if(found != null){
                    return found;
                }
            }
        }

        return null;
    }

}
