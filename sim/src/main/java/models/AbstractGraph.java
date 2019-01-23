package models;

import org.jgrapht.graph.DirectedMultigraph;
import org.jgrapht.graph.SimpleDirectedGraph;

public class AbstractGraph extends DirectedMultigraph {

    public AbstractGraph(Class<Edge> edgeClass)
    {
        super(edgeClass);
    }
}
