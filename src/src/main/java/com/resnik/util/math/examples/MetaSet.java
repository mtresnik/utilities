package com.resnik.util.math.examples;

import com.resnik.util.math.symbo.operations.Constant;
import com.resnik.util.math.symbo.operations.Operation;
import com.resnik.util.math.symbo.operations.base.Negation;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class MetaSet extends LinkedHashSet<Operation>{

    public Operation n;
    public Operation m;

    public static MetaSet upper(int n){
        return upper(new Constant(n));
    }

    public static MetaSet upper(Operation n){
        MetaSet ret = new MetaSet();
        ret.n = n;
        ret.m = Constant.ONE;
        ret.add(new Negation(n));
        ret.add(n);
        return ret;
    }

    public static MetaSet lower(int m){
        return lower(new Constant(m));
    }

    public static MetaSet lower(Operation m){
        MetaSet ret = new MetaSet();
        ret.n = Constant.ONE;
        ret.m = m;
        if(m.allConstants()){
            Constant rep = m.constantRepresentation();
            if(rep.isInteger()){
                int intRep = (int) Math.abs(rep.getValue().real);
                if(intRep > 0){
                    ret.addAll(upper(m));
                    if(intRep != 1){
                        ret.addAll(lower(new Constant(intRep - 2)));
                    }
                }else{
                    ret.add(Constant.ZERO);
                }
            }
        }
        return sort(ret);
    }

    public static MetaSet upperLower(int n, int m){
        return upperLower(new Constant(n), new Constant(m));
    }

    public static MetaSet upperLower(Operation n, Operation m){
        MetaSet lowerSet = lower(m);
        MetaSet ret = new MetaSet();
        ret.n = n;
        ret.m = m;
        for(Operation o : lowerSet){
            ret.add(o.multiply(n));
        }
        return sort(ret);
    }

    public static MetaSet sort(MetaSet input){
        List<Constant> retList = new ArrayList<>();
        for(Operation o : input){
            if(!o.allConstants()){
                return input;
            }
            retList.add(o.constantRepresentation());
        }
        retList.sort(Constant.REAL_COMPARATOR);
        MetaSet ret = new MetaSet();
        ret.n = input.n;
        ret.m = input.m;
        ret.addAll(retList);
        return ret;
    }

    public MetaSet sort(){
        MetaSet replace = sort(this);
        this.clear();
        this.addAll(replace);
        return this;
    }

    public static MetaSet upperAddition(MetaSet one, MetaSet two){
        if(!one.isUpper() || !two.isUpper()){
            return null;
        }
        if(one.n.equals(two.n)){
            return upperLower(one.n, Constant.TWO);
        }
        return upper(one.n.add(two.n)).union(upper(one.n.subtract(two.n)));
    }

    public boolean isUpper(){
        if(n == null) return false;
        if(m == null || m.equals(Constant.ONE)) return true;
        return false;
    }

    public MetaSet union(MetaSet other){
        this.n = null;
        this.m = null;
        this.addAll(other);
        return this;
    }

    public Operation sum(){
        Operation ret = Constant.ZERO;
        for(Operation o : this){
            ret = ret.add(o);
        }
        return ret;
    }

    public String toString(){
        return "M(" + (n == null ? "?" : n) + "," + (m == null ? "?" : m) + ")=" + super.toString();
    }

    public static void main(String[] args) {
        MetaSet a = upper(2);
        System.out.println(a);
        System.out.println(upperAddition(a, a));
        System.out.println(lower(50));
        System.out.println(a.sum());
    }


}
