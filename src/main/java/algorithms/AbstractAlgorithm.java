package algorithms;

import models.AbstractNode;
import models.Graph;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public abstract class AbstractAlgorithm {

	public abstract void initRandomGraph(int graphSize,int cacheSize,int shuffleLength) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;

	public abstract void initGraphFromCSV(String path, int cacheSize, int shuffleLength);

	public abstract void initGraph(Graph g,int cacheSize, int shuffleLength);
	//public abstract void initGraph(int n);

	public abstract AbstractNode nextPeer(Integer nodeLabel);

	public abstract Graph getGraph();
	
	public ArrayList<Integer> getTestSample(int sampleSize) {
		ArrayList<Integer> chosenPeers = new ArrayList<Integer>();
		for(int i=0; i<sampleSize; i++) {
			AbstractNode nextPeer = this.nextPeer(0);
			chosenPeers.add(nextPeer.getLabel());
		}
		return chosenPeers;
	}
}

