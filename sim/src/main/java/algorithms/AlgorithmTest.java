package algorithms;

import java.util.Iterator;
import java.util.Set;

import models.*;

public class AlgorithmTest extends AbstractAlgorithm {

	private Graph<SimpleNode,SimpleEdge> graph;

	public void initGraph() {
		this.graph = new Graph<SimpleNode,SimpleEdge>(SimpleNode.class,SimpleEdge.class);
		this.graph.addVertex(new SimpleNode(1));
		this.graph.addVertex(new SimpleNode(2));
	}

	@Override
	public void shuffle(int nodeLabel, int shuffleLength) {
		
	}

	@Override
	public double chiSquaredCompute() {
		// TODO
		return 0.0;
	}

	@Override
	public AbstractNode nextPeer() {
		return new EmptyNode();
	}
}
