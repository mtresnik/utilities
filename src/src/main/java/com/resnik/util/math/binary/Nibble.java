package com.resnik.util.math.binary;

public class Nibble extends BitCollection {

    public static final int SIZE = 4;

    public Nibble(boolean b1, boolean b2, boolean b3, boolean b4) {
        super(b1,b2,b3,b4);
    }

    public Nibble(Bit b1, Bit b2, Bit b3, Bit b4) {
        super(b1,b2,b3,b4);
    }

    public Nibble(int b1, int b2, int b3, int b4) {
        super(b1,b2,b3,b4);
    }

    public static Nibble allZeros() {
        return new Nibble(0,0,0,0);
    }

    public static Nibble allOnes() {
        return new Nibble(1,1,1,1);
    }

    public static Nibble random(){
        return new Nibble(Bit.random(), Bit.random(), Bit.random(), Bit.random());
    }

    public Nibble or(Nibble other){
        Bit[] bitRep = this.bitRep();
        Bit[] otherRep = other.bitRep();
        Bit[] rep = new Bit[SIZE];
        for(int i = 0; i < SIZE; i++){
            rep[i] = bitRep[i].or(otherRep[i]);
        }
        return new Nibble(rep[0], rep[1], rep[2], rep[3]);
    }

    public Nibble and(Nibble other){
        Bit[] bitRep = this.bitRep();
        Bit[] otherRep = other.bitRep();
        Bit[] rep = new Bit[SIZE];
        for(int i = 0; i < SIZE; i++){
            rep[i] = bitRep[i].and(otherRep[i]);
        }
        return new Nibble(rep[0], rep[1], rep[2], rep[3]);
    }

    public Nibble not(){
        Bit[] bitRep = this.bitRep();
        Bit[] rep = new Bit[SIZE];
        for(int i = 0; i < SIZE; i++){
            rep[i] = bitRep[i].not();
        }
        return new Nibble(rep[0], rep[1], rep[2], rep[3]);
    }

    public Nibble xor(Nibble other){
        Bit[] bitRep = this.bitRep();
        Bit[] otherRep = other.bitRep();
        Bit[] rep = new Bit[SIZE];
        for(int i = 0; i < SIZE; i++){
            rep[i] = bitRep[i].xor(otherRep[i]);
        }
        return new Nibble(rep[0], rep[1], rep[2], rep[3]);
    }

}
