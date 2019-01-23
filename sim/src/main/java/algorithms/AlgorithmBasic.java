package algorithms;

import models.AbstractNode;
import models.Graph;
import models.SimpleNode;

public class AlgorithmBasic extends AbstractAlgorithm{

    private Graph<SimpleNode> graph;

    @Override
    public void initGraph() {
        this.graph = new Graph<SimpleNode>(SimpleNode.class);
    }

    @Override
    public void shuffle() {

    }

    @Override
    public AbstractNode nextPeer() {
        return null;
    }
}
