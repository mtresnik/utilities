package com.resnik.util.objects.structures.tree;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class Tree<T> implements Collection<T> {

    protected TreeNode<T> root;

    public Tree(){}

    public Tree(TreeNode<T> root) {
        this.root = root;
    }

    @Override
    public int size() {
        if(this.isEmpty()){
            return 0;
        }
        return this.root.children.size() + 1;
    }

    @Override
    public boolean isEmpty() {
        return root == null;
    }

    @Override
    public boolean contains(Object o) {
        if(this.isEmpty()){
            return false;
        }
        if(o instanceof TreeNode){
            return this.root.getAllNodes().contains(o);
        }
        return this.root.getAllValues().contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return this.root.getAllValues().iterator();
    }

    public List<T> toListValues(){
        if (this.root == null) {
            return null;
        }
        List<T> retList = root.getValueList();
        return retList;
    }

    public List<TreeNode<T>> toListNodes(){
        if(this.root == null){
            return null;
        }
        return root.getNodeList();
    }

    @Override
    public Object[] toArray() {
        return this.root.getAllValues().toArray();
    }

    @Override
    public <T> T[] toArray(T[] ts) {
        return this.root.getAllValues().toArray(ts);
    }

    @Override
    public boolean add(T t) {
        if(t == null){
            throw new IllegalArgumentException("Cannot add a null element.");
        }
        if(this.root == null){
            this.root = this.generateNode(t, null);
            return true;
        }
        return this.root.addChild(generateNode(t, root));
    }

    public boolean addChild(TreeNode<T> child){
        if(child == null){
            throw new IllegalArgumentException("Cannot add a null child.");
        }
        if(this.root == null){
            this.root = child;
            return true;
        }
        return this.root.addChild(child);
    }

    @Override
    public boolean remove(Object o) {
        if(this.isEmpty()){
            return false;
        }
        if(Objects.equals(this.root, o) || Objects.equals(this.root.value, o)){
            this.root = null;
            return true;
        }
        if(o instanceof TreeNode){
            ((TreeNode) o).remove();
        }else if(o.getClass().equals(this.root.value.getClass())){
            T elem = (T) o;
            TreeNode<T> toRem = this.root.get(elem);
            if(toRem == null){
                return false;
            }
            return toRem.remove();
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends T> collection) {
        boolean ret = true;
        for(T elem : collection){
            ret &= this.add(elem);
        }
        return ret;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        if(this.isEmpty()){
            return false;
        }
        boolean ret = true;
        for(Object elem : collection){
            ret &= this.remove(elem);
        }
        return ret;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        return false;
    }

    @Override
    public void clear() {
        this.root = null;
    }

    @Override
    public String toString() {
        return root.toString();
    }

    public TreeNode<T> generateNode(T value, TreeNode<T> parent){
        return new TreeNode<>(value, parent);
    }

    public TreeNode<T> getRoot() {
        return root;
    }
}
