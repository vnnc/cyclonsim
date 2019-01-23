package algorithms;

import java.util.Iterator;
import java.util.Set;

import models.*;

public class AlgorithmTest extends AbstractAlgorithm {

	private Graph<SimpleNode> graph;
	public void initGraph() {
		this.graph = new Graph<SimpleNode>(SimpleNode.class);
		this.graph.addVertex(new SimpleNode(1));
		this.graph.addVertex(new SimpleNode(2));
	}
	
	public void shuffle() {

	}
	
	public void testKhi2() {
		// TODO
	}
	
	public AbstractNode nextPeer() {
		return new NodeTest(2);
	}
}
