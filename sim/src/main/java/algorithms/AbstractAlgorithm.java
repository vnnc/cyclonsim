package algorithms;

import models.AbstractNode;
import models.Graph;

import java.util.ArrayList;

public abstract class AbstractAlgorithm {
	public abstract void initRandomGraph(int graphSize,int cacheSize);
	public abstract void initGraphFromCSV(String path,int cacheSize);
	public abstract void shuffle(int nodeLabel, int shuffleLength);
	public abstract void round();
	public abstract AbstractNode nextPeer();
	
	public abstract ArrayList<Integer> getChosenPeers();
}
