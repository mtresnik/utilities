package com.resnik.util.math.plot.elements3d;

public enum PointType {
    SPHERE(10), CUBE(30);

    public int size;

    private PointType(int size){
        this.size = size;
    }
}
