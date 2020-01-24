package com.resnik.util.objects.structures.graph;

public class VertexEntry {

    VertexEntry previous;
    Vertex currentVertex;

    public VertexEntry(VertexEntry previous, Vertex currentVertex) {
        this.previous = previous;
        this.currentVertex = currentVertex;
    }
}
