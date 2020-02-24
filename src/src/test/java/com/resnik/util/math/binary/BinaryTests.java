package com.resnik.util.math.binary;

import org.junit.Test;

import java.util.Arrays;

public class BinaryTests {

    @Test
    public void testByte(){
        Byte b1 = new Byte(1,0,0,1,0,0,1,0);
        System.out.println(b1);
    }

    @Test
    public void testByte2(){
        byte bN = (byte) 127;
        System.out.println(bN);
        Byte bC = Byte.fromByte(bN);
        System.out.println(bC);
        byte bT = bC.toByte();
        System.out.println(bT);
    }

    @Test
    public void testNibble(){
        Nibble n1 = new Nibble(1,0,0,1);
        Nibble n2 = new Nibble(0,0,1,1);
        Byte b2 = new Byte(n1, n2);
        System.out.println(b2);
        System.out.println(Arrays.toString(b2.toNibbles()));
    }

    @Test
    public void testWord(){
        Byte b1 = new Byte(1,0,0,1,0,0,1,0);
        Nibble n1 = new Nibble(1,0,0,1);
        Nibble n2 = new Nibble(0,0,1,1);
        Byte b2 = new Byte(n1, n2);
        Word w = new Word(b1, b2);
        System.out.println(w);
        System.out.println(Arrays.toString(w.toBytes()));
        System.out.println(Arrays.toString(w.toNibbles()));
    }

    @Test
    public void testLongWord(){
        LongWord l1 = LongWord.allOnes();
        LongWord l2 = LongWord.random();
        System.out.println(l1);
        System.out.println(l2);
        System.out.println(l1.xor(l2));
    }

    @Test
    public void testLongWord2(){
        int num = -1;
        System.out.println(num);
        LongWord lw1 = LongWord.fromInt(num);
        System.out.println(lw1);
    }

}
