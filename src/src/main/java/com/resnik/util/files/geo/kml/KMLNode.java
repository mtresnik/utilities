package com.resnik.util.files.geo.kml;

import com.resnik.util.files.xml.XMLNode;
import com.resnik.util.objects.structures.tree.TreeNode;

public class KMLNode extends XMLNode<KMLElement> {

    public KMLNode(KMLElement value) {
        super(value);
    }

    public KMLNode(KMLElement value, KMLNode parent) {
        super(value, parent);
    }

    public void setName(String newName){
        this.setInner("name", newName);
    }

    public final void setDescription(String description){
        this.setInner("description", description);
    }

    public void setInner(String tag, String inner){
        for(TreeNode<KMLElement> child : this.getChildren()){
            if(child.getValue().getName().equals(tag)){
                child.getValue().setInner(inner);
                return;
            }
        }
        this.addChild(new KMLNode(new KMLElement(tag, inner), null));
    }

    protected void replaceChild(KMLNode newChild){
        TreeNode<KMLElement> toReplace = null;
        for(TreeNode<KMLElement> child : this.getChildren()){
            if(child.getValue().getName().equals(newChild.getValue().getName())){
                toReplace = child;
                break;
            }
        }
        if(toReplace != null){
            this.getChildren().remove(toReplace);
        }
        this.addChild(newChild);
    }

    protected void removeChildByTag(String tag){
        TreeNode<KMLElement> toReplace = null;
        for(TreeNode<KMLElement> child : this.getChildren()){
            if(child.getValue().getName().equals(tag)){
                toReplace = child;
                break;
            }
        }
        if(toReplace != null){
            this.getChildren().remove(toReplace);
        }
    }

    protected String getInner(String tag){
        for(TreeNode<KMLElement> child : this.getChildren()){
            if(child.getValue().getName().equals(tag)){
                return child.getValue().getInner();
            }
        }
        return null;
    }

    public void setID(String id){
        this.getValue().put("id", id);
    }
}
