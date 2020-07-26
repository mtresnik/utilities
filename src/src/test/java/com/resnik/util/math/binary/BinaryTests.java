package com.resnik.util.math.binary;

import com.resnik.util.logger.Log;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BinaryTests {

    public static final String TAG = BinaryTests.class.getSimpleName();

    @Test
    public void testByte(){
        Byte b1 = new Byte(1,0,0,1,0,0,1,0);
        Log.v(TAG,b1);
    }

    @Test
    public void testByte2(){
        byte bN = (byte) 127;
        Log.v(TAG,bN);
        Byte bC = Byte.fromByte(bN);
        Log.v(TAG,bC);
        byte bT = bC.toByte();
        Log.v(TAG,bT);
    }

    @Test
    public void testNibble(){
        Nibble n1 = new Nibble(1,0,0,1);
        Nibble n2 = new Nibble(0,0,1,1);
        Byte b2 = new Byte(n1, n2);
        Log.v(TAG,b2);
        Log.v(TAG,Arrays.toString(b2.toNibbles()));
    }

    @Test
    public void testWord(){
        Byte b1 = new Byte(1,0,0,1,0,0,1,0);
        Nibble n1 = new Nibble(1,0,0,1);
        Nibble n2 = new Nibble(0,0,1,1);
        Byte b2 = new Byte(n1, n2);
        Word w = new Word(b1, b2);
        Log.v(TAG,w);
        Log.v(TAG,Arrays.toString(w.toBytes()));
        Log.v(TAG,Arrays.toString(w.toNibbles()));
    }

    @Test
    public void testLongWord(){
        LongWord l1 = LongWord.allOnes();
        LongWord l2 = LongWord.random();
        Log.v(TAG,l1);
        Log.v(TAG,l2);
        Log.v(TAG,l1.xor(l2));
    }

    @Test
    public void testLongWord2(){
        int num = -1;
        Log.v(TAG,num);
        LongWord lw1 = LongWord.fromInt(num);
        Log.v(TAG,lw1);
    }

    @Test
    public void testFunction(){
        List<boolean[]> inputs = new ArrayList<>();
        int size = 3;
        for(int i = 0; i <= 1; i++){
            boolean a = i > 0;
            for(int j = 0; j <= 1; j++){
                boolean b = j > 0;
                for(int k = 0; k <= 1; k++){
                    boolean c = k > 0;
                    boolean[] input = new boolean[]{a, b, c};
                    inputs.add(input);
                }
            }
        }
        for(boolean[] input : inputs){
            boolean a = input[0];
            boolean b = input[1];
            boolean x = input[2];

            int aInt = a ? 1 : 0;
            int aBarInt = !a ? 1 : 0;
            int bInt = b ? 1 : 0;
            int bBarInt = !b ? 1 : 0;
            int xInt = 4;

            int[] intInputs = new int[]{aInt, bInt, xInt};

            int result = 3*aInt + 2*bInt + aBarInt*bBarInt*xInt;

            System.out.println(Arrays.toString(intInputs) + "\t=" + result + "\t->" + (result>0));


        }

    }

}
