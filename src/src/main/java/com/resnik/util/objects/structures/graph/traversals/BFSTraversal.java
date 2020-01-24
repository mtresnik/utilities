package com.resnik.util.objects.structures.graph.traversals;

import com.resnik.util.objects.structures.graph.Edge;
import com.resnik.util.objects.structures.graph.Graph;
import com.resnik.util.objects.structures.graph.Path;
import com.resnik.util.objects.structures.graph.Vertex;

import java.util.*;

public class BFSTraversal extends Traversal{

    @Override
    public Path getPath(Vertex start, Vertex end, Graph graph) {
        if(!graph.contains(start) || !graph.contains(end)) return null;
        Path retPath = new Path();
        Queue<Vertex> queue = new ArrayDeque<>();
        Set<Vertex> visited = new LinkedHashSet<>();
        Map<Vertex, Edge> previousMap = new LinkedHashMap<>();
        previousMap.put(start, null);
        Vertex current = start;
        while(!current.equals(end)){
            Set<Edge> connectedEdges = current.getConnectedEdges(graph);
            for(Edge edge : connectedEdges){
                Vertex to = edge.to(current);
                if(!visited.contains(to)){
                    visited.add(to);
                    previousMap.put(to, edge);
                    queue.add(to);
                }
            }
            if(queue.isEmpty()) return null;
            current = queue.poll();
        }
        return Traversal.backtrack(start, end, previousMap);
    }

}
