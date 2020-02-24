package com.resnik.util.objects.structures.tree.kd;

public class KDTreeValue<DATA> {


    double[] point;
    DATA data;


    public KDTreeValue(double[] point, DATA data) {
        this.point = point;
        this.data = data;
    }
}
