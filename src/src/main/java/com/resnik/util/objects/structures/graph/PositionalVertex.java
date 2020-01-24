package com.resnik.util.objects.structures.graph;

public class PositionalVertex extends Vertex {

    public double[] position;

    public PositionalVertex(String name, double[] position) {
        super(name);
        this.position = position;
    }

    public PositionalVertex(double[] position) {
        this.position = position;
    }


}
