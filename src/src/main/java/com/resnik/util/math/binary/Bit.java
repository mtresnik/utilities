package com.resnik.util.math.binary;

public class Bit {

    public static final int SIZE = 1;

    public boolean rep;

    public Bit(int rep){
        this(rep == 1);
    }

    public Bit(boolean rep){
        this.rep = rep;
    }

    public int intRep(){
        return rep ? 1 : 0;
    }

    public Bit or(Bit other){
        return new Bit(rep || other.rep);
    }

    public Bit and(Bit other){
        return new Bit(rep && other.rep);
    }

    public Bit not(){
        return new Bit(!rep);
    }

    public Bit xor(Bit other){
        return new Bit(intRep() ^ other.intRep());
    }

    public String toString(){
        return Integer.toString(intRep());
    }

    public static Bit[] fromBooleanArray(boolean ... input){
        Bit[] ret = new Bit[input.length];
        for(int i = 0; i < input.length; i++){
            ret[i] = new Bit(input[i]);
        }
        return ret;
    }

    public static Bit[] fromIntArray(int ... input){
        Bit[] ret = new Bit[input.length];
        for(int i = 0; i < input.length; i++){
            ret[i] = new Bit(input[i]);
        }
        return ret;
    }

    public static boolean[] toBooleanArray(Bit ... input){
        boolean[] ret = new boolean[input.length];
        for(int i = 0; i < input.length; i++){
            ret[i] = input[i].rep;
        }
        return ret;
    }

    public static int[] toIntArray(Bit ... input){
        int[] ret = new int[input.length];
        for(int i = 0; i < input.length; i++){
            ret[i] = input[i].intRep();
        }
        return ret;
    }

    public static Bit random(){
        return new Bit(Math.random() > 0.5);
    }


}
