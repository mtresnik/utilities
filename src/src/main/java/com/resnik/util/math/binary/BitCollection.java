package com.resnik.util.math.binary;

public abstract class BitCollection {

    private Bit[] rep;

    public BitCollection(int ... rep){
        this(Bit.fromIntArray(rep));
    }

    public BitCollection(boolean ... rep){
        this(Bit.fromBooleanArray(rep));
    }

    public BitCollection(Bit ... rep){
        this.rep = rep;
    }

    public int length(){
        return rep.length;
    }

    public int size(){
        return rep.length;
    }

    public String toString(){
        String ret = "";
        for(Bit b : rep){
            ret += b.toString();
        }
        return ret;
    }

    public Bit[] bitRep(){
        Bit[] retArr = new Bit[this.rep.length];
        for(int i = 0; i < this.rep.length; i++){
            retArr[i] = new Bit(this.rep[i].rep);
        }
        return retArr;
    }

    public int intRep(){
        return Integer.parseInt(this.toString(), 2);
    }

    public boolean boolAt(int index){
        if(index >= rep.length){
            return false;
        }
        return rep[index].rep;
    }

}
