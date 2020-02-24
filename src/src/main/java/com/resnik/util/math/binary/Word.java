package com.resnik.util.math.binary;

import com.resnik.util.objects.arrays.ArrayUtils;

import java.util.Arrays;

public class Word extends BitCollection {

    public static final int SIZE = 2*Byte.SIZE;

    public Word(Byte b1, Byte b2){
        super(ArrayUtils.yoke(b1.bitRep(), b2.bitRep()));
    }

    Word(Bit[] bitRep){
        super(bitRep);
        if(bitRep.length != SIZE){
            throw new IllegalArgumentException();
        }
    }

    public short toShort(){
        return Short.parseShort(this.toString(), 2);
    }

    public static Word allZeros() {
        return new Word(Byte.allZeros(), Byte.allZeros());
    }

    public static Word allOnes() {
        return new Word(Byte.allOnes(), Byte.allOnes());
    }

    public static Word random(){
        return new Word(Byte.random(), Byte.random());
    }

    public Nibble[] toNibbles(){
        Byte[] byteRep = toBytes();
        return ArrayUtils.yoke(byteRep[0].toNibbles(), byteRep[1].toNibbles());
    }

    public Byte[] toBytes(){
        Bit[] bitRep = this.bitRep();
        Byte[] ret = new Byte[2];
        Bit[] a = Arrays.copyOf(bitRep, Byte.SIZE);
        Bit[] b = Arrays.copyOfRange(bitRep, Byte.SIZE, bitRep.length);
        ret[0] = new Byte(a);
        ret[1] = new Byte(b);
        return ret;
    }

    public Word or(Word other){
        Byte[] bytes = this.toBytes();
        Byte b1 = bytes[0];
        Byte b2 = bytes[1];
        Byte[] otherBytes = other.toBytes();
        Byte b3 = otherBytes[0];
        Byte b4 = otherBytes[1];
        return new Word(b1.or(b3), b2.or(b4));
    }

    public Word and(Word other){
        Byte[] bytes = this.toBytes();
        Byte b1 = bytes[0];
        Byte b2 = bytes[1];
        Byte[] otherBytes = other.toBytes();
        Byte b3 = otherBytes[0];
        Byte b4 = otherBytes[1];
        return new Word(b1.and(b3), b2.and(b4));
    }

    public Word not(){
        Byte[] bytes = this.toBytes();
        Byte b1 = bytes[0];
        Byte b2 = bytes[1];
        return new Word(b1.not(), b2.not());
    }

    public Word xor(Word other){
        Byte[] bytes = this.toBytes();
        Byte b1 = bytes[0];
        Byte b2 = bytes[1];
        Byte[] otherBytes = other.toBytes();
        Byte b3 = otherBytes[0];
        Byte b4 = otherBytes[1];
        return new Word(b1.xor(b3), b2.xor(b4));
    }


}
