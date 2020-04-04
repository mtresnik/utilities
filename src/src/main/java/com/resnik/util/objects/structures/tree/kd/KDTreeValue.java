package com.resnik.util.objects.structures.tree.kd;

import java.util.Arrays;

public class KDTreeValue<DATA> {


    public double[] point;
    public DATA data;


    public KDTreeValue(double[] point, DATA data) {
        this.point = point;
        this.data = data;
    }

    @Override
    public String toString() {
        return "{" +
                "point=" + Arrays.toString(point) +
                ", data=" + data +
                '}';
    }
}
