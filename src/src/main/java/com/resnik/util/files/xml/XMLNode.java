package com.resnik.util.files.xml;

import com.resnik.util.objects.structures.tree.TreeNode;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class XMLNode<T extends XMLElement> extends TreeNode<T> {

    public XMLNode(T value, TreeNode<T> parent){
        super(value, parent);
        if(value.element != null){
            NodeList childList = value.element.getChildNodes();
            for(int i = 0; i < childList.getLength(); i++){
                Node currNode = childList.item(i);
                if(!currNode.getNodeName().equals("#text")){
                    XMLElement curr = new XMLElement(currNode);
                    this.getChildren().add(new XMLNode(curr, this));
                }
            }
        }
    }

    public XMLNode(T value){
        super(value, null);
    }

    @Override
    public String toString(){
        return toStringDelim(0);
    }

    public String toStringDelim(int tabOffset){
        String retString = "";
        String tabString = "";
        for(int i = 0; i < tabOffset; i++){
            tabString += "\t";
        }
        retString += tabString;
        retString += "<" + this.getValue().name;
        if(!this.getValue().isEmpty()){
            retString += " ";
        }
        retString += this.getValue().toString();
        if(this.hasChildren()){
            retString += ">";
            retString += "\n";
            for(TreeNode<T> child : this.getChildren()){
                if(child instanceof XMLNode){
                    retString += ((XMLNode) child).toStringDelim(tabOffset + 1);
                    retString += "\n";
                }
            }
            retString += tabString;
            retString += "</" + this.getValue().name + ">";
        }else {
            if(!this.getValue().inner.isEmpty()){
                retString += ">";
                retString += "\n";
                retString += "\t" + tabString + this.getValue().inner.replaceAll("\n", "\n\t" + tabString) + "\n";
                retString += tabString + "</" + this.getValue().name + ">";
            }else {
                retString += "/>";
            }
        }
        return retString;
    }

    public String getName(){
        return this.getValue().name;
    }

    public List<XMLNode> findChildrenByName(String name){
        List<XMLNode> retList = new ArrayList<>();
        for(TreeNode<T> child : this.getChildren()){
            if(child instanceof XMLNode){
                if(Objects.equals(((XMLNode) child).getName(), name)){
                    retList.add((XMLNode) child);
                }
            }
        }
        return retList;
    }

    public List<XMLNode> findAllByName(String name){
        List<XMLNode> retList = new ArrayList<>();
        if(Objects.equals(this.getName(), name)){
            retList.add(this);
        }
        for(TreeNode<T> child : this.getChildren()){
            if(child instanceof XMLNode){
                retList.addAll(((XMLNode) child).findAllByName(name));
            }
        }
        return retList;
    }

    public Double parseInnerDouble(){
        if(this.getValue() != null){
            XMLElement xmlElement = (XMLElement) this.getValue();
            return Double.parseDouble(xmlElement.getInner());
        }
        return null;
    }

    public static XMLNode fromTag(final String TAG){
        XMLElement retElement = new XMLElement(TAG);
        return new XMLNode(retElement);
    }

    public static XMLNode fromTagValue(final String TAG, final String VALUE){
        XMLElement retElement = new XMLElement(TAG);
        retElement.setInner(VALUE);
        return new XMLNode(retElement);
    }

    public static XMLNode fromTagValue(final String TAG, final Double VALUE){
        XMLElement retElement = new XMLElement(TAG);
        retElement.setInner(Double.toString(VALUE));
        return new XMLNode(retElement);
    }

    public static XMLNode fromTagValue(final String TAG, final Integer VALUE){
        XMLElement retElement = new XMLElement(TAG);
        retElement.setInner(Integer.toString(VALUE));
        return new XMLNode(retElement);
    }

}
