package com.resnik.util.objects.structures.tree;

import com.resnik.util.objects.structures.Node;
import org.junit.Test;

import java.util.Arrays;

import static com.resnik.util.objects.structures.tree.binary.BinaryTree.generateBinaryGif;

public class TestTree {

    @Test
    public void testBinaryGif(){
        generateBinaryGif("src/res/nodes/binaryGif.gif", Arrays.asList(3.0, 2.0,5.0,4.0,2.5, 1.0,6.0, -1.0, 1.5, 7.0, 3.5,4.5, 2.2,2.8));
    }

    @Test
    public void testBinaryTree(){
        Node<Integer> root = Node.constructBinaryTree(1,2,3,4,5);
        System.out.println(root.contains(6));
    }

    @Test
    public void testTree(){
        SimpleTree<Double> tree = new SimpleTree<>();
        tree.add(10.0);
        tree.add(5.0);
        tree.add(7.0);
        System.out.println(tree.size());
        System.out.println(tree);
        System.out.println(tree.contains(8.0));
        System.out.println(tree.contains(7.0));
    }
}
