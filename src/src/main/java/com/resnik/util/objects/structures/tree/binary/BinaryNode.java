package com.resnik.util.objects.structures.tree.binary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class BinaryNode<K> {

    public BinaryNode<K> left, right;
    public K value;
    public BinaryNode<K> parent;

    public BinaryNode(K value) {
        this(value, null); // Root call
    }

    public BinaryNode(K value, BinaryNode<K> parent) {
        if (value == null) {
            throw new IllegalArgumentException("Value can never be null.");
        }
        this.value = value;
        this.parent = parent;
    }

    private boolean insertLeft(K inputVal) {
        if (left == null) {
            left = new BinaryNode(inputVal, this);
            return true;
        }
        return left.insert(inputVal);
    }

    private boolean insertRight(K inputVal) {
        if (right == null) {
            right = new BinaryNode(inputVal, this);
            return true;
        }
        return right.insert(inputVal);
    }

    public int getComp(K inputVal) {
        int comp = 0;
        if (inputVal instanceof Comparable) {
            Comparable cval = (Comparable) value;
            Comparable cinput = (Comparable) inputVal;
            comp = cinput.compareTo(cval);
        } else {
            String val_string = Objects.toString(value);
            String input_string = Objects.toString(inputVal);
            comp = input_string.compareTo(val_string);
        }
        return comp;
    }

    public int getComp(BinaryNode<K> node) {
        return getComp(node.value);
    }

    public boolean insert(K inputVal) {
        if (inputVal == null) {
            return false;
        }
        int comp = getComp(inputVal);
        if (comp < 0) {
            return insertLeft(inputVal);
        }
        if (comp == 0) {
            return false;
        }
        return insertRight(inputVal);

    }

    private boolean insertLeft(BinaryNode<K> node) {
        if (left == null) {
            left = node;
            node.parent = this;
            return true;
        }
        return left.insert(node);
    }

    private boolean insertRight(BinaryNode<K> node) {
        if (right == null) {
            right = node;
            node.parent = this;
            return true;
        }
        return right.insert(node);
    }

    public boolean insert(BinaryNode<K> node) {
        if (node == null) {
            return false;
        }
        int comp = getComp(node);
        if (comp <= 0) {
            return insertLeft(node);
        }
        return insertRight(node);
    }

    private boolean containsLeft(K inputVal) {
        if (left == null) {
            return false;
        }
        return left.contains(inputVal);
    }

    private boolean containsRight(K inputVal) {
        if (right == null) {
            return false;
        }
        return right.contains(inputVal);
    }
    
    public boolean containsNode(BinaryNode<K> node) {
        if (node == null) {
            return false;
        }
        return containsValue(node.value) && this.getNode(node.value).getHeight() == node.getHeight();
    }

    public boolean containsValue(K inputVal) {
        int comp = getComp(inputVal);
        if (comp == 0) {
            return true;
        }
        if (comp < 0) {
            return containsLeft(inputVal);
        } else {
            return containsRight(inputVal);
        }
    }

    public boolean contains(Object inputObject) {
        if (inputObject == null) {
            return false;
        }
        if (inputObject instanceof BinaryNode) {
            return containsNode((BinaryNode) inputObject);
        }
        K inputVal = null;
        try {
            inputVal = (K) inputObject;
        } catch (Exception e) {
            return false;
        }
        return containsValue(inputVal);
    }

    public BinaryNode<K> getNode(K inputValue) {
        if (inputValue == null) {
            return null;
        }
        if (Objects.equals(this.value, inputValue)) {
            return this;
        }
        if (left == null && right == null) {
            return null;
        }
        if (left != null) {
            BinaryNode<K> leftRes = left.getNode(inputValue);
            if (leftRes != null) {
                return leftRes;
            }
        }
        if (right != null) {
            BinaryNode<K> rightRes = right.getNode(inputValue);
            if (rightRes != null) {
                return rightRes;
            }
        }
        return null;
    }

    public List<BinaryNode<K>> getAllNodes(K inputValue) {
        if (inputValue == null) {
            return null;
        }
        List<BinaryNode<K>> retList = new ArrayList();
        if (Objects.equals(this.value, inputValue)) {
            retList.add(this);
        }
        if (left != null) {
            List<BinaryNode<K>> leftRes = left.getAllNodes(inputValue);
            if (leftRes != null) {
                retList.addAll(leftRes);
            }
        }
        if (right != null) {
            List<BinaryNode<K>> rightRes = right.getAllNodes(inputValue);
            if (rightRes != null) {
                retList.addAll(rightRes);
            }
        }
        return retList;
    }

    public int size() {
        int retSize = 1;
        retSize += leftSize();
        retSize += rightSize();
        return retSize;
    }

    public int leftSize() {
        if (left == null) {
            return 0;
        }
        return left.size();
    }

    public int rightSize() {
        if (right == null) {
            return 0;
        }
        return right.size();
    }

    public BinaryState heavy() {
        int leftSize = leftSize();
        int rightSize = rightSize();
        if (leftSize > rightSize) {
            return BinaryState.LEFT_HEAVY;
        } else if (rightSize > leftSize) {
            return BinaryState.RIGHT_HEAVY;
        }
        return BinaryState.EQUAL;
    }

    public int getHeight() {
        int leftHeight = leftHeight();
        int rightHeight = rightHeight();
        return Math.max(leftHeight, rightHeight) + 1;
    }

    public int leftHeight() {
        if (left == null) {
            return 0;
        }
        return left.getHeight();
    }

    public int rightHeight() {
        if (right == null) {
            return 0;
        }
        return right.getHeight();
    }

    public int balanceFactor() {
        return leftHeight() - rightHeight();
    }

    public boolean isBalanced() {
        return Math.abs(balanceFactor()) <= 1;
    }

    @Override
    public String toString() {
        return Objects.toString(value);
    }

    public String childString() {
        return this.toString() + " -> " + Arrays.toString(new BinaryNode[]{this.left, this.right});
    }

    public List<K> getListRepSorted() {
        List<K> retList = new ArrayList();
        if (left != null) {
            List<K> leftList = left.getListRepSorted();
            retList.addAll(leftList);
        }
        retList.add(value);
        if (right != null) {
            List<K> rightList = right.getListRepSorted();
            retList.addAll(rightList);
        }
        return retList;
    }

    public List<BinaryNode<K>> getListNodeSorted() {
        List<BinaryNode<K>> retList = new ArrayList();
        if (left != null) {
            List<BinaryNode<K>> leftList = left.getListNodeSorted();
            retList.addAll(leftList);
        }
        retList.add(this);
        if (right != null) {
            List<BinaryNode<K>> rightList = right.getListNodeSorted();
            retList.addAll(rightList);
        }
        return retList;
    }

}
