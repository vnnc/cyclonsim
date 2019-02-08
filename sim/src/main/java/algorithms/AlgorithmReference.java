package algorithms;

import models.AbstractNode;
import models.Graph;
import models.SimpleEdge;
import models.SimpleNode;
import utilities.Utilities;

import java.util.ArrayList;
import java.util.Random;

public class AlgorithmReference extends AbstractAlgorithm {

	private Graph<SimpleNode, SimpleEdge> graph;
	private Random rng = new Random();
	private ArrayList<Integer> chosenPeers = new ArrayList<Integer>();

	// taille du 'cache' dans l'article cyclon
	private int cacheSize;

	// l dans l'article cyclon
	private int shuffleLength;

	private int i = 0;

	@Override
	public void initRandomGraph(int graphSize,int cacheSize,int shuffleLength) {
		this.graph = new Graph<SimpleNode, SimpleEdge>(SimpleNode.class,SimpleEdge.class);
		this.graph.generateRandom(graphSize, 0.3);
		this.cacheSize = cacheSize;
		this.shuffleLength = shuffleLength;
	}

	@Override
	public void initGraphFromCSV(String path,int cacheSize,int shuffleLength) {
		this.graph = new Graph<SimpleNode,SimpleEdge>(SimpleNode.class,SimpleEdge.class);
		this.graph.importFromCSV(path);
		this.cacheSize = cacheSize;
		this.shuffleLength = shuffleLength;
	}

	@Override
	public void initGraph(Graph g,int cacheSize,int shuffleLength) {
		this.graph = g;
		this.cacheSize = cacheSize;
		this.shuffleLength = shuffleLength;
	}

	@Override
	public void shuffle(int nodeLabel) {

	}

	@Override
	public void shuffleAll() {

	}

	@Override
	public AbstractNode nextPeer(Object n) {
		int size = this.graph.vertexSet().size();
		//Utilities.printDebug("nextPeer: "+(i%size));
		return this.graph.getNodeByLabel((i++)%size);
	}

	@Override
	public ArrayList<Integer> getChosenPeers() {
		return null;
	}

	@Override
	public Graph getGraph() {
		return this.graph;
	}
}
