package algorithms;

import models.*;
import utilities.Utilities;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class AlgorithmCyclonBasic extends AbstractAlgorithm {

	private Graph<SimpleNode, SimpleEdge> graph;
	private Random rng = new Random();
	private ArrayList<Integer> chosenPeers = new ArrayList<Integer>();
    int counter= 0;
	// taille du 'cache' dans l'article cyclon
	private int cacheSize;

	// l dans l'article cyclon
	private int shuffleLength;
	
	public static int nbEchanges = 0;
	public static int nbEchecs = 0;

	@Override
	public void initRandomGraph(int graphSize, int cacheSize, int shuffleLength) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
		this.graph = new Graph<SimpleNode, SimpleEdge>(SimpleNode.class,SimpleEdge.class);
		this.graph.generateRandom(graphSize, cacheSize);
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
	public void shuffleAll() {
		for(int i=0; i<this.graph.vertexSet().size(); i++) {

			this.shuffle(i);
		}
		this.counter++;
	}

	@Override
	public AbstractNode nextPeer(Integer nodeLabel){
		Object n = this.getGraph().getNodeByLabel(nodeLabel);
		ArrayList<SimpleNode> neighbors = this.graph.getNeighborsOfNode((SimpleNode) n);
		if(neighbors.size() > 0) {
			Random r = new Random();
			int index = r.nextInt(neighbors.size());
			return neighbors.get(index);
		} else {
			return new EmptyNode();
		}
	}

	private ArrayList<SimpleNode> getRandomSubset(ArrayList<SimpleNode> nodes,int size) {
		ArrayList<SimpleNode> subset = new ArrayList<SimpleNode>();
		subset.addAll(nodes);
		Collections.shuffle(subset);
		subset = new ArrayList<SimpleNode>(subset.subList(0,size));
		return subset;
	}

	public void shuffle(int nodePLabel) {
		/* Each peer P repeatedly initiates a neighbor exchange operation, known
		as shuffle, by executing the following six steps */

		Utilities.printDebug("Graph before shuffle: " + this.graph);
		SimpleNode nodeP = this.graph.getNodeByLabel(nodePLabel);
		ArrayList<SimpleNode> nodePNeighbors = this.graph.getNeighborsOfNode(nodeP);
		Utilities.printDebug("Neighbors of node P " + nodeP + ": " + nodePNeighbors);
		
		/* 1. Select a random subset of l neighbors (1 ≤ l ≤ c) from P's own cache,
		and a random peer, Q, within this subset, where l is a system parameter,
		called shuffle length. */
		int pNeighborsSubsetSize = Math.min(shuffleLength, nodePNeighbors.size());
		if(nodePNeighbors.size() <= 0) {
			Utilities.printDebug("[Error during shuffle, P has no neighbor] Graph: " + this.graph);
			return;
		}

		ArrayList<SimpleNode> nodePNeighborsSubset = this.getRandomSubset(nodePNeighbors, pNeighborsSubsetSize);

		SimpleNode peerQ = nodePNeighborsSubset.get(rng.nextInt(nodePNeighborsSubset.size()));
		peerQ = this.graph.getNodeByLabel(peerQ.getLabel()); // On va chercher l'objet correspondant à peerQ dans le graph

		// peer added to choosenPeers for chi-squared test computations
		this.chosenPeers.add(peerQ.getLabel());
		Utilities.printDebug("Chosen peer Q: " + peerQ);
		
		/* 3. Send the updated subset to Q. */
		ArrayList<SimpleNode> subsetForPeerQ = new ArrayList<SimpleNode>();
		for(SimpleNode n : nodePNeighborsSubset) {
			if(n.getLabel() == peerQ.getLabel()) {
				subsetForPeerQ.add(nodeP);
			} else {
				subsetForPeerQ.add(n);
			}
		}
		Utilities.printDebug("Subset sent to peer: " + subsetForPeerQ);
		
		/* 4. Receive from Q a subset of no more than l of Q's neighbors. */
		ArrayList<SimpleNode> peerQNeighbors = this.graph.getNeighborsOfNode(peerQ);
		Utilities.printDebug("Peer Q neighbors: " + peerQNeighbors);
		int peerQNeighborsSubsetSize = Math.min(shuffleLength, peerQNeighbors.size());
		if (peerQNeighbors.size() <= 0) {
			Utilities.printDebug("[Error during shuffle, Q has no neighbor] Graph: " + this.graph);
			return;
		}

		ArrayList<SimpleNode> peerQNeighborsSubset = this.getRandomSubset(peerQNeighbors, peerQNeighborsSubsetSize);

		ArrayList<SimpleEdge> newEdges = new ArrayList<SimpleEdge>();
		ArrayList<SimpleEdge> removedEdges = new ArrayList<SimpleEdge>();
		ArrayList<SimpleNode> nodePKeptEntries = new ArrayList<SimpleNode>();

		ArrayList<SimpleNode> disposables = new ArrayList<SimpleNode>(subsetForPeerQ);
		disposables.remove(nodeP);


		for (SimpleNode n : peerQNeighborsSubset) {
			n = this.graph.getNodeByLabel(n.getLabel());

			if (this.graph.getEdge(n, nodeP) == null
			&& this.graph.getEdge(nodeP, n) == null
			&& nodeP != n) {
				if(nodePNeighbors.size()<cacheSize){

					nodePKeptEntries.add(n);

					this.graph.addEdge(nodeP,n);

					newEdges.add(this.graph.getEdge(nodeP,n));
					nodePNeighbors.add(n);
				} else if (disposables.size() > 0) {
					SimpleNode removeTarget = disposables.get(disposables.size()-1);
					SimpleEdge toRemove = this.graph.getEdge(nodeP, removeTarget);
					if(toRemove != null) {
						this.graph.removeEdge(toRemove);
						disposables.remove(removeTarget);
						removedEdges.add(toRemove);

						nodePKeptEntries.add(n);
						this.graph.addEdge(nodeP, n);
						newEdges.add(this.graph.getEdge(nodeP, n));
						nodePNeighbors.add(n);
					}
				}
			}
		}

		Utilities.printDebug("Remaining entries within subset from peer Q: " + nodePKeptEntries);
		
		ArrayList<SimpleNode> peerQKeptEntries = new ArrayList<SimpleNode>();

		disposables = new ArrayList<SimpleNode>(peerQNeighborsSubset);

		for(SimpleNode n : subsetForPeerQ){
			n = this.graph.getNodeByLabel(n.getLabel());

			if (this.graph.getEdge(n, peerQ) == null
			&& this.graph.getEdge(peerQ, n) == null
			&& peerQ != n) {
				if (peerQNeighbors.size() < cacheSize) {
					peerQKeptEntries.add(n);
					this.graph.addEdge(peerQ, n);
					newEdges.add(this.graph.getEdge(peerQ, n));
					peerQNeighbors.add(n);
				} else if (disposables.size() > 0){
					SimpleNode removeTarget = disposables.get(disposables.size()-1);
					SimpleEdge toRemove = this.graph.getEdge(peerQ, removeTarget);

					if(toRemove != null) {
						this.graph.removeEdge(toRemove);
						disposables.remove(removeTarget);
						removedEdges.add(toRemove);

						peerQKeptEntries.add(n);
						this.graph.addEdge(peerQ, n);
						newEdges.add(this.graph.getEdge(peerQ, n));
						peerQNeighbors.add(n);
					}
				}
			}
		}
		
		if (newEdges.isEmpty() || removedEdges.isEmpty()) {
			Utilities.printDebug("Nothing happened");
			this.nbEchecs++;
		} else {
			Utilities.printDebug("Remaining entries within subset from node P: " + peerQKeptEntries);
			Utilities.printDebug("Created Edges: " + newEdges);
			Utilities.printDebug("Removed Edges: " + removedEdges);
			
			Utilities.printDebug("Graph after shuffle: " + this.graph);
			this.nbEchanges++;
		}
	}

	//@Override
	public ArrayList<Integer> getChosenPeers() {
		return this.chosenPeers;
	}

	@Override
	public Graph getGraph()
	{
		return this.graph;
	}
}

