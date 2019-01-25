package algorithms;

import models.AbstractNode;
import models.Graph;

public abstract class AbstractAlgorithm {
	public abstract void initGraph();
	public abstract void shuffle(int nodeLabel,int shuffleLength);
	public abstract double chiSquaredCompute();
	public abstract AbstractNode nextPeer();
}
