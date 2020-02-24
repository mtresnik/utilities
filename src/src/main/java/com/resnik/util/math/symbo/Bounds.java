package com.resnik.util.math.symbo;


import java.util.ArrayList;
import java.util.List;

public class Bounds {

    public static final Bounds DEFAULT_10 = new Bounds(-10, 10);
    
    public ComplexNumber min, max;

    public Bounds(ComplexNumber min, ComplexNumber max) {
        this.min = min;
        this.max = max;
    }
    
    public Bounds(double min, double max){
        this.min = ComplexNumber.a(min);
        this.max = ComplexNumber.a(max);
    }

    public List<Double> toDoubleList(int separations){
        List<Double> retList = new ArrayList<>();
        separations = Math.abs(separations);
        double delta = 0.0;
        if(separations != 0){
            delta = (max.real - min.real) / separations;
        }
        if(delta != 0.0){
            for(double curr = min.real; curr < max.real; curr += delta){
                retList.add(curr);
            }
        }else{
            retList.add(min.real);
        }
        retList.add(max.real);
        retList.sort(Double::compareTo);
        return retList;
    }

    @Override
    public String toString() {
        return "Bounds{" + "min=" + min + ", max=" + max + '}';
    }
    
    public boolean contains(double real){
        if(real < this.min.real){
            return false;
        }
        if(real > this.max.real){
            return false;
        }
        return true;
    }

    public ComplexNumber size(){
        return max.subtract(min);
    }
    
    
}
