package com.resnik.util.objects.structures.tree.kd;

import com.resnik.util.objects.structures.tree.TreeNode;

public class KDTreeNode<DATA> extends TreeNode<KDTreeValue<DATA>> {

    KDTreeNode<DATA> left, right;

    public KDTreeNode(KDTreeValue value, TreeNode parent) {
        super(value, parent);
    }

}
