package algorithms;

import models.AbstractNode;
import models.Graph;

public abstract class AbstractAlgorithm {
	
	public abstract void initGraph();
	
	public abstract void shuffle();
	
	public void testKhi2() {
		// TODO
	}
	
	public abstract AbstractNode nextPeer();
}
