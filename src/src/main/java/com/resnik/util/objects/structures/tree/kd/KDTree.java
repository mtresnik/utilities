package com.resnik.util.objects.structures.tree.kd;

import com.resnik.util.logger.Log;
import com.resnik.util.objects.structures.tree.Tree;

public class KDTree<DATA> extends Tree<KDTreeValue<DATA>, KDTreeNode<DATA>> {

    public final int dimensions;

    public KDTree(int dimensions) {
        if(dimensions <= 0){
            throw new IllegalArgumentException("Must have positive dimensions");
        }
        this.dimensions = dimensions;
    }

    public KDTreeNode<DATA> insert(KDTreeValue<DATA> value){
        return insert(value, (KDTreeNode<DATA>)root, 0);
    }

    private KDTreeNode<DATA> insert(KDTreeValue<DATA> value, KDTreeNode<DATA> curr, int cx){
        if(root == null){
            KDTreeNode<DATA> tempRoot = new KDTreeNode<>(value, null);
            root = tempRoot;
            return tempRoot;
        }
        if(curr == null){
            return new KDTreeNode<>(value, null);
        }
        if(value.point[cx] < curr.value.point[cx]){
            curr.left = insert(value, curr.left, (cx + 1) % dimensions);
            curr.left.setParent(curr);
        }else{
            curr.right = insert(value, curr.right, (cx + 1) % dimensions);
            curr.right.setParent(curr);
        }
        return curr;
    }

    public KDTreeNode<DATA> closest(double[] coords){
        return closest(coords, (KDTreeNode<DATA>) root, 0);
    }

    private KDTreeNode<DATA> closest(double[] coords, KDTreeNode<DATA> curr, int cx){
        if(curr == null || root == null){
            return null;
        }
        if(!root.hasChildren()){
            return (KDTreeNode<DATA>)root;
        }
        KDTreeNode<DATA> temp = null;
        if(coords[cx] < curr.value.point[cx]){
            temp = closest(coords, curr.left, (cx + 1) % dimensions);
            if(temp == null){
                if(curr.left == null){
                    return curr;
                }
                return curr.left;
            }
            curr = closest(coords, curr.left, (cx + 1) % dimensions);
        }else{
            temp = closest(coords, curr.right, (cx + 1) % dimensions);
            if(temp == null){
                if(curr.right == null){
                    return curr;
                }
                return curr.right;
            }
            curr = closest(coords, curr.right, (cx + 1) % dimensions);
        }
        return curr;
    }

}
