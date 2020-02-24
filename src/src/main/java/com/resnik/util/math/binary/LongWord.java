package com.resnik.util.math.binary;

import com.resnik.util.objects.arrays.ArrayUtils;

import java.math.BigInteger;
import java.util.Arrays;

public class LongWord extends BitCollection {

    public static final int SIZE = 2*Word.SIZE;

    public LongWord(Word w1, Word w2){
        super(ArrayUtils.yoke(w1.bitRep(), w2.bitRep()));
    }

    public LongWord(Byte b1, Byte b2, Byte b3, Byte b4){
        this(new Word(b1, b2), new Word(b3, b4));
    }

    public static LongWord allZeros() {
        return new LongWord(Word.allZeros(), Word.allZeros());
    }

    public static LongWord allOnes() {
        return new LongWord(Word.allOnes(), Word.allOnes());
    }

    public static LongWord random(){
        return new LongWord(Word.random(), Word.random());
    }

    public Nibble[] toNibbles(){
        Word[] words = toWords();
        return ArrayUtils.yoke(words[0].toNibbles(), words[1].toNibbles());
    }

    public Byte[] toBytes(){
        Word[] words = toWords();
        return ArrayUtils.yoke(words[0].toBytes(), words[1].toBytes());
    }

    public Word[] toWords(){
        Bit[] bitRep = this.bitRep();
        Word[] ret = new Word[2];
        Bit[] a = Arrays.copyOf(bitRep, Word.SIZE);
        Bit[] b = Arrays.copyOfRange(bitRep, Word.SIZE, bitRep.length);
        ret[0] = new Word(a);
        ret[1] = new Word(b);
        return ret;
    }

    public int toInt(){
        return Integer.parseInt(this.toString(), 2);
    }

    public static LongWord fromInt(int input){
        String binaryString = Integer.toBinaryString(input);
        while(binaryString.length() < SIZE){
            binaryString = "0" + binaryString;
        }
        int[] bitRep = new int[SIZE];
        for(int i = 0; i < bitRep.length; i++){
            char currChar = binaryString.charAt(i);
            bitRep[i] = currChar == '1' ? 1 : 0;
        }
        int[] b1Arr = Arrays.copyOf(bitRep, Byte.SIZE);
        int[] b2Arr = Arrays.copyOfRange(bitRep, Byte.SIZE, Byte.SIZE*2);
        int[] b3Arr = Arrays.copyOfRange(bitRep, Byte.SIZE*2, Byte.SIZE*3);
        int[] b4Arr = Arrays.copyOfRange(bitRep,Byte.SIZE*3, Byte.SIZE*4);
        Byte b1 = new Byte(b1Arr);
        Byte b2 = new Byte(b2Arr);
        Byte b3 = new Byte(b3Arr);
        Byte b4 = new Byte(b4Arr);
        return new LongWord(b1, b2, b3, b4);
    }

    public LongWord or(LongWord other){
        Word[] bytes = this.toWords();
        Word b1 = bytes[0];
        Word b2 = bytes[1];
        Word[] otherBytes = other.toWords();
        Word b3 = otherBytes[0];
        Word b4 = otherBytes[1];
        return new LongWord(b1.or(b3), b2.or(b4));
    }

    public LongWord and(LongWord other){
        Word[] bytes = this.toWords();
        Word b1 = bytes[0];
        Word b2 = bytes[1];
        Word[] otherBytes = other.toWords();
        Word b3 = otherBytes[0];
        Word b4 = otherBytes[1];
        return new LongWord(b1.and(b3), b2.and(b4));
    }

    public LongWord not(){
        Word[] bytes = this.toWords();
        Word b1 = bytes[0];
        Word b2 = bytes[1];
        return new LongWord(b1.not(), b2.not());
    }

    public LongWord xor(LongWord other){
        Word[] bytes = this.toWords();
        Word b1 = bytes[0];
        Word b2 = bytes[1];
        Word[] otherBytes = other.toWords();
        Word b3 = otherBytes[0];
        Word b4 = otherBytes[1];
        return new LongWord(b1.xor(b3), b2.xor(b4));
    }

}
