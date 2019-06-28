package util.structures;

import java.util.Arrays;

public class Node<T> {

    public T value;
    public Node<T>[] children;
    public boolean visited;

    public Node(T value, Node<T>... children) {
        this.value = value;
        this.children = children;
        this.visited = false;
    }

    public void visit() {
        this.visited = true;
    }

    public void unvisit() {
        this.visited = false;
    }

    public void visitChildren() {
        this.visit();
        for (Node<T> temp : this.children) {
            if (temp == null) {
                continue;
            }
            if (temp.visited == false) {
                temp.visit();
            }
        }
    }

    public void unvisitChildren() {
        this.unvisit();
        for (Node<T> temp : this.children) {
            if (temp == null) {
                continue;
            }
            if (temp.visited == true) {
                temp.unvisit();
            }
        }

    }

    public void visitConnectingAll() {
        this.visit();
        for (Node<T> temp : this.children) {
            if (temp == null) {
                continue;
            }
            if (temp.visited == false) {
                temp.visitConnectingAll();
            }
        }
    }

    public void unvisitConnectingAll() {
        this.unvisit();
        for (Node<T> temp : this.children) {
            if (temp == null) {
                continue;
            }
            if (temp.visited == true) {
                temp.unvisitConnectingAll();
            }
        }
    }

    public boolean containsSingle(T query) {
        if (query == null) {
            return false;
        }
        return query.equals(this.value);
    }

    public boolean childrenContains(T query) {
        boolean retValue = false;
        for (Node<T> child : this.children) {
            if (child == null) {
                continue;
            }
            if (child.containsSingle(query)) {
                retValue |= true;
                break;
            }
        }
        return retValue;
    }

    public boolean contains(T query) {
        boolean retBool = false;
        this.visit();
        if (this.containsSingle(query)) {
            retBool |= true;
        } else {
            for (Node<T> child : this.children) {
                if (child == null) {
                    continue;
                }
                if (child.visited == false) {
                    retBool |= child.contains(query);
                    if (retBool) {
                        break;
                    }
                }
            }
        }
        this.unvisit();
        return retBool;
    }

    public static Node<Integer> constructBinaryTree(int... values) {
        int[] clone = Arrays.copyOf(values, values.length);
        Arrays.sort(clone);
        return recursiveBinaryTree(clone);
    }

    private static Node<Integer> recursiveBinaryTree(int[] arr) {
        if (arr.length == 0) {
            return null;
        }
        if (arr.length == 1) {
            return new Node(new Integer(arr[0]), null, null);
        }
        int middle = arr[arr.length / 2];
        int m_index = arr.length / 2;
        int[] left = Arrays.copyOf(arr, arr.length / 2);
        int[] right = Arrays.copyOfRange(arr, arr.length / 2 + 1, arr.length);
        Node<Integer> leftNode = recursiveBinaryTree(left);
        Node<Integer> rightNode = recursiveBinaryTree(right);
        Node<Integer> currNode = new Node(middle, leftNode, rightNode);
        return currNode;
    }

}
