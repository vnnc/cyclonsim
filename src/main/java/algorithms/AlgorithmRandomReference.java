package algorithms;

import models.*;
import utilities.Utilities;

import java.util.*;

public class AlgorithmRandomReference extends AbstractAlgorithm {

	private Graph<SimpleNode, SimpleEdge> graph;
	private Random rng = new Random();
	private int graphSize;

	@Override
	public void initRandomGraph(int graphSize, int cacheSize, int shuffleLength) {
		this.graphSize = graphSize;
	}

	@Override
	public void initGraphFromCSV(String path, int cacheSize, int shuffleLength) {
		//TODO ??
	}

	@Override
	public void initGraph(Graph g, int cacheSize,int shuffleLength) {
		this.graphSize = g.vertexSet().size();
		this.graph = g;
	}

	@Override
	public AbstractNode nextPeer(Integer label){
		AbstractNode node = new SimpleNode(rng.nextInt(this.graphSize));
		return node;
	}

	@Override
	public Graph getGraph() {
		return this.graph;
	}
}

