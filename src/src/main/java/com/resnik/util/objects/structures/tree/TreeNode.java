package com.resnik.util.objects.structures.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TreeNode<T> {
    T value;
    TreeNode<T> parent;
    List<TreeNode<T>> children = new ArrayList<>();

    public TreeNode(T value, TreeNode parent) {
        this.value = value;
        this.parent = parent;
    }

    public List<T> getValueList(){
        List<T> retList = new ArrayList<>();
        List<TreeNode<T>> nodeList = this.getNodeList();
        nodeList.forEach((elem) -> {retList.add(elem.value);});
        return retList;
    }

    public List<T> getAllValues(){
        List<T> retList = new ArrayList<>();
        for(TreeNode<T> child : children){
            retList.addAll(child.getAllValues());
        }
        retList.add(this.value);
        return retList;
    }

    public List<TreeNode<T>> getAllNodes(){
        List<TreeNode<T>> retList = new ArrayList<>();
        for(TreeNode<T> child : children){
            retList.addAll(child.getAllNodes());
        }
        retList.add(this);
        return retList;
    }

    public List<TreeNode<T>> getNodeList(){
        List<TreeNode<T>> retList = new ArrayList<>();
        retList.addAll(this.children);
        retList.add(this);
        return retList;
    }

    public boolean add(T elem){
        return this.children.add(new TreeNode<>(elem, this));
    }

    public boolean addAll(Iterable<T> iterable){
        boolean ret = true;
        for(T t : iterable){
            ret &= this.add(t);
        }
        return ret;
    }

    public boolean addChild(TreeNode<T> child){
        return this.children.add(child);
    }

    public TreeNode<T> get(T elem){
        if(Objects.equals(this.value, elem)){
            return this;
        }
        for(TreeNode<T> child : this.children){
            if(child.get(elem) != null){
                return child;
            }
        }
        return null;
    }

    public boolean contains(Object elem){
        if(Objects.equals(this.value, elem) || Objects.equals(this, elem)){
            return true;
        }
        for(TreeNode<T> child : this.children){
            if(child.contains(elem)){
                return true;
            }
        }
        return false;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public void setParent(TreeNode<T> parent) {
        this.parent = parent;
    }

    public void setChildren(List<TreeNode<T>> children) {
        this.children = children;
    }

    public T getValue() {
        return value;
    }

    public TreeNode<T> getParent() {
        return parent;
    }

    public List<TreeNode<T>> getChildren() {
        return children;
    }

    public boolean hasChildren(){
        return !this.children.isEmpty();
    }

    public boolean remove(){
        if(this.parent == null){
            return false;
        }
        boolean ret = this.parent.children.remove(this);
        this.parent = null;
        return ret;
    }

    @Override
    public String toString() {
        return "{" +
                "value=" + value +
                (children.size() > 0? ", children=" + children : "")
                 +
                '}';
    }
}
