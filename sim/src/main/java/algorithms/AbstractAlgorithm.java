package algorithms;

import models.AbstractNode;
import models.Graph;

import java.util.ArrayList;

public abstract class AbstractAlgorithm {

	public abstract void initRandomGraph(int graphSize,int cacheSize,int shuffleLength);

	public abstract void initGraphFromCSV(String path,int cacheSize,int shuffleLength);

	public abstract void initGraph(Graph g,int cacheSize,int shuffleLength);

	public abstract void shuffle(int nodeLabel);

	public abstract void shuffleAll();

	public abstract AbstractNode nextPeer(Object n);
	
	public abstract ArrayList<Integer> getChosenPeers();

	public abstract Graph getGraph();

	public void multiShuffleAll(int amount)
	{
		for(int i=0;i<amount;i++)
		{
			this.shuffleAll();
		}
	}

}
