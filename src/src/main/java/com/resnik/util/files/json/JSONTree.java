package com.resnik.util.files.json;

import com.resnik.util.files.xml.XMLTree;
import com.resnik.util.objects.structures.tree.Tree;
import com.resnik.util.objects.structures.tree.TreeNode;

public class JSONTree extends Tree<JSONElement> {

    public JSONTree(JSONNode root){
        super(root);
    }

    @Override
    public TreeNode<JSONElement> generateNode(JSONElement value, TreeNode parent) {
        return JSONNode.decide(value, (JSONNode) parent);
    }

    public static JSONTree generateFromObject(Object object){
        if(object instanceof JSONNode){
            return new JSONTree((JSONNode) object);
        }
        if(object instanceof JSONTree){
            return (JSONTree) object;
        }
        if(object instanceof JSONElement){
            return new JSONTree(JSONNode.decide((JSONElement) object, null));
        }
        JSONElement element = JSONElement.decide("root", object);
        JSONNode rep = JSONNode.decide(element, null);
        return new JSONTree(rep);
    }


    public XMLTree toXMLTree(){
        if(this.root == null){
            return new XMLTree();
        }
        JSONNode jsonNode = (JSONNode) root;
        XMLTree retTree = new XMLTree();
        retTree.addChild(jsonNode.toXMLNode());
        return retTree;
    }

}
