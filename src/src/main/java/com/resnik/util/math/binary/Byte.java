package com.resnik.util.math.binary;

import com.resnik.util.objects.arrays.ArrayUtils;

import java.util.Arrays;

public class Byte extends BitCollection{

    public static final int SIZE = 2*Nibble.SIZE;

    public Byte(boolean b1, boolean b2, boolean b3, boolean b4, boolean b5, boolean b6, boolean b7, boolean b8) {
        super(b1,b2,b3,b4,b5,b6,b7,b8);
    }

    public Byte(Bit b1, Bit b2, Bit b3, Bit b4, Bit b5, Bit b6, Bit b7, Bit b8) {
        super(b1,b2,b3,b4,b5,b6,b7,b8);
    }

    Byte(Bit[] bitRep){
        super(bitRep);
        if(bitRep.length != SIZE){
            throw new IllegalArgumentException();
        }
    }

    public static Byte allZeros() {
        return new Byte(Nibble.allZeros(), Nibble.allZeros());
    }

    public static Byte allOnes() {
        return new Byte(Nibble.allOnes(), Nibble.allOnes());
    }

    public static Byte random(){
        return new Byte(Nibble.random(), Nibble.random());
    }

    public Byte(int b1, int b2, int b3, int b4, int b5, int b6, int b7, int b8) {
        super(b1,b2,b3,b4,b5,b6,b7,b8);
    }

    Byte(int[] bitRep){
        super(bitRep);
        if(bitRep.length != SIZE){
            throw new IllegalArgumentException();
        }
    }

    public Byte(Nibble n1, Nibble n2){
        super(ArrayUtils.yoke(n1.bitRep(), n2.bitRep()));
    }

    public char toChar(){
        return (char) this.intRep();
    }

    public static String bytesToString(Byte ... bytes){
        String retString = "";
        for(Byte b : bytes){
            retString += b.toChar();
        }
        return retString;
    }

    public static Byte fromByte(byte b){
        int intRep = b;
        if(intRep < 0){
            intRep += 256;
        }
        int[] bitRep = new int[SIZE];
        for(int i = 7; i >= 0; i--){
            int curr = (int) (Math.pow(2.0, i));
            if(intRep >= curr){
                bitRep[7 - i] = 1;
                intRep -= curr;
            }else{
                bitRep[7 - i] = 0;
            }
        }
        return new Byte(bitRep);
    }

    public byte toByte(){
        return (byte) this.intRep();
    }

    public static Byte fromChar(char c){
        int intRep = c;
        int[] bitRep = new int[8];
        for(int i = 7; i >= 0; i--){
            int curr = (int) (Math.pow(2.0, i));
            if(intRep >= curr){
                bitRep[7 - i] = 1;
                intRep -= curr;
            }else{
                bitRep[7 - i] = 0;
            }
        }
        return new Byte(bitRep);
    }

    public static Byte[] fromString(String str){
        if(str == null){
            return null;
        }
        char[] charRep = str.toCharArray();
        Byte[] ret = new Byte[charRep.length];
        for(int i = 0; i < charRep.length; i++){
            ret[i] = fromChar(charRep[i]);
        }
        return ret;
    }

    public Nibble[] toNibbles(){
        Bit[] bitRep = this.bitRep();
        Nibble n1 = new Nibble(bitRep[0], bitRep[1], bitRep[2], bitRep[3]);
        Nibble n2 = new Nibble(bitRep[4], bitRep[5], bitRep[6], bitRep[7]);
        return new Nibble[]{n1, n2};
    }

    public Byte or(Byte other){
        Nibble[] nibbles = this.toNibbles();
        Nibble n1 = nibbles[0];
        Nibble n2 = nibbles[1];
        Nibble[] otherNibbles = other.toNibbles();
        Nibble n3 = otherNibbles[0];
        Nibble n4 = otherNibbles[1];
        return new Byte(n1.or(n3), n2.or(n4));
    }

    public Byte and(Byte other){
        Nibble[] nibbles = this.toNibbles();
        Nibble n1 = nibbles[0];
        Nibble n2 = nibbles[1];
        Nibble[] otherNibbles = other.toNibbles();
        Nibble n3 = otherNibbles[0];
        Nibble n4 = otherNibbles[1];
        return new Byte(n1.and(n3), n2.and(n4));
    }

    public Byte not(){
        Nibble[] nibbles = this.toNibbles();
        Nibble n1 = nibbles[0];
        Nibble n2 = nibbles[1];
        return new Byte(n1.not(), n2.not());
    }

    public Byte xor(Byte other){
        Nibble[] nibbles = this.toNibbles();
        Nibble n1 = nibbles[0];
        Nibble n2 = nibbles[1];
        Nibble[] otherNibbles = other.toNibbles();
        Nibble n3 = otherNibbles[0];
        Nibble n4 = otherNibbles[1];
        return new Byte(n1.xor(n3), n2.xor(n4));
    }

}
