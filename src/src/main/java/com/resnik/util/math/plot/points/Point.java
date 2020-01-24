package com.resnik.util.math.plot.points;


import com.resnik.util.math.symbo.ComplexNumber;

public abstract class Point {

    private ComplexNumber[] values;

    public Point(ComplexNumber ... values){
        this.values = values;
    }
    
    public ComplexNumber[] getValues() {
        return values;
    }
    
}
