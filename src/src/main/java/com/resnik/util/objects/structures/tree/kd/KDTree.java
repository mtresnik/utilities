package com.resnik.util.objects.structures.tree.kd;

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
        return insert(value, null, 0);
    }

    private KDTreeNode<DATA> insert(KDTreeValue<DATA> value, KDTreeNode<DATA> curr, int cx){
        if(root == null){
            KDTreeNode<DATA> tempRoot = new KDTreeNode<>(value, null);
            root = tempRoot;
            return tempRoot;
        }
        if(curr == null){
            curr = new KDTreeNode<>(value, null);
        } else if(value.point[cx] < curr.value.point[cx]){
            curr.left = insert(value, curr.left, (cx + 1) & dimensions);
        }else{
            curr.right = insert(value, curr.right, (cx + 1) & dimensions);
        }
        return curr;
    }

}
