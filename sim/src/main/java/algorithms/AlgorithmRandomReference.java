package algorithms;

import models.*;
import utilities.Utilities;

import java.util.*;

public class AlgorithmRandomReference extends AbstractAlgorithm {

	private Graph<SimpleNode, SimpleEdge> graph;
	private Random rng = new Random();


	@Override
	public void initRandomGraph(int graphSize, int cacheSize, int shuffleLength) {}

	@Override
	public void initGraphFromCSV(String path,int cacheSize,int shuffleLength) {
		//TODO ??
	}

	@Override
	public void initGraph(Graph g,int cacheSize,int shuffleLength) {}

	@Override
	public void shuffleAll() {}

	@Override
	public AbstractNode nextPeer(Integer label){
		//TODO
		return new EmptyNode();
	}

	@Override
	public Graph getGraph() {
		return this.graph;
	}
}

