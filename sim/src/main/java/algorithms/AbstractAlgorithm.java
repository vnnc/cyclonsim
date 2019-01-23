package algorithms;

import models.AbstractNode;
import models.Graph;

public abstract class AbstractAlgorithm {
	protected Graph graph;
	
	public abstract void initGraph();
	
	public abstract void shuffle();
	
	public void testKhi2() {
		// TODO
	}
	
	public abstract AbstractNode nextPeer();
}
