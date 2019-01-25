package algorithms;

import models.AbstractNode;
import models.Graph;

import java.util.ArrayList;

public abstract class AbstractAlgorithm {
	public abstract void initGraph(int graphSize);
	public abstract void shuffle(int nodeLabel, int shuffleLength);
	public abstract void round();
	public abstract AbstractNode nextPeer();
	
	public abstract ArrayList<Integer> getChosenPeers();
}
