package models;

import org.jgrapht.graph.DirectedMultigraph;

public class Graph extends DirectedMultigraph {

    public Graph(Class<? extends  Edge> edgeClass)
    {
        super(edgeClass);
    }
}
