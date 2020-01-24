package com.resnik.util.files.json;

import com.resnik.util.files.xml.XMLNode;
import com.resnik.util.objects.structures.tree.TreeNode;

import java.util.List;

public class JSONArrayNode extends JSONNode<JSONElement> {

    public JSONArrayNode(JSONElement value, TreeNode<JSONElement> parent) {
        super(value, parent);
        if(value instanceof JSONArrayElement){
            JSONArrayElement _value = ((JSONArrayElement) value);
            List l = (_value.rep);
            int count = 0;
            for(Object obj : l){
                String childName =  _value.getChildName(count);
                childName = childName.replaceAll("\"", "");
                JSONElement elementDecision = JSONElement.decide("\"" + childName+ "\"", obj);
                JSONNode jsonNode = JSONNode.decide(elementDecision, this);
                this.addChild(jsonNode);
                count++;
            }
        }else{
            throw new IllegalArgumentException("Value must be of type: JSONArrayElement");
        }
    }

    @Override
    public String getName() {
        return this.getValue().name;
    }

    @Override
    public JSONObjectNode toJSONObjectNode() {
        return new JSONObjectNode(this.getValue().toJSONObjectElement(), this.getParent());
    }

    @Override
    public XMLNode toXMLNode() {
        return this.toJSONObjectNode().toXMLNode();
    }

    public String toString(){
        String retString = this.getValue().name + ":[";
        int count = 0;
        for(TreeNode node : this.getChildren()){
            retString += node.toString();
            if(count < this.getChildren().size() - 1){
                retString += ", ";
            }
            count++;
        }
        retString += "]";
        return retString;
    }



}
