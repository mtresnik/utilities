package com.resnik.util.objects.structures.graph.traversals;

import com.resnik.util.objects.structures.CollectionUtils;
import com.resnik.util.objects.structures.graph.Edge;
import com.resnik.util.objects.structures.graph.Graph;
import com.resnik.util.objects.structures.graph.Path;
import com.resnik.util.objects.structures.graph.Vertex;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public abstract class Traversal {

    public abstract Path getPath(Vertex start, Vertex end, Graph graph);

    public static Path backtrack(Vertex start, Vertex end, Map<Vertex, Edge> previousMap){
        Set<Vertex> retSet = new LinkedHashSet<>();
        Vertex current = end;
        retSet.add(current);
        double totalWeight = 0.0;
        while(!current.equals(start)){
            Edge previousEdge = previousMap.get(current);
            totalWeight += previousEdge.getWeight();
            Vertex from = previousEdge.from(current);
            current = from;
            retSet.add(current);
        }
        return new Path(CollectionUtils.reverse(retSet), totalWeight);
    }

}
