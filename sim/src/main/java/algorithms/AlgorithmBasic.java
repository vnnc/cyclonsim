package algorithms;

import models.AbstractNode;
import models.SimpleEdge;
import models.Graph;
import models.SimpleNode;

import java.util.*;

//TODO utiliser les mêmes termes qu'eux :
// - maxPeers (>=2) (= shuffleLength ?)
// - round(s) (= appeler la fonction shuffle sur tous les pairs)
// - partialView == peerNeighbors
// - etc.

//mettre un cache (= liste de voisins) (= partialView)

public class AlgorithmBasic extends AbstractAlgorithm {

	private Graph<SimpleNode, SimpleEdge> graph;
	private Random rng = new Random();
	private ArrayList<Integer> chosenPeers = new ArrayList<Integer>();
	private int cacheSize;

	@Override
	public void initGraph(int graphSize,int cacheSize) {
		this.graph = new Graph<SimpleNode, SimpleEdge>(SimpleNode.class,SimpleEdge.class);
		this.graph.generateRandom(graphSize, 0.3);
		this.cacheSize = cacheSize;
	}

	@Override
	public void round() { //XXX c'est de la merde je pense
		for(int i=0; i<this.graph.vertexSet().size(); i++) {
			for(int j=0; j<this.graph.vertexSet().size(); j++) {
				this.shuffle(j, 2);
			}
		}
	}

	@Override //XXX y a-t-il vraiment besoin que ce truc soit abstrait puis overridé ?
	public void shuffle(int nodeLabel, int shuffleLength) {
		/* Each peer P repeatedly initiates a neighbor exchange operation, known
		as shuffle, by executing the following six steps */
		System.out.println("Graph before shuffle: " + this.graph);
		SimpleNode node = this.graph.getNodeByLabel(nodeLabel);
		ArrayList<SimpleNode> nodeNeighbors = this.graph.getNeighborsOfNode(node);
		System.out.println("Neighbors of node " + node + ": " + nodeNeighbors);
		
		/* 1. Select a random subset of l neighbors (1 ≤ l ≤ c) from P’s own cache,
		and a random peer, Q, within this subset, where l is a system parameter,
		called shuffle length. */
		int neighborsSubsetSize = Math.min(shuffleLength, nodeNeighbors.size());
		if(nodeNeighbors.size() > 0) {
			ArrayList<SimpleNode> nodeNeighborsSubset = new ArrayList<SimpleNode>();
			nodeNeighborsSubset.addAll(nodeNeighbors);
			Collections.shuffle(nodeNeighborsSubset);
			System.out.println("DEBUG: nodeNeighborsSubset: " + nodeNeighborsSubset);
			
			nodeNeighborsSubset = new ArrayList<SimpleNode>(nodeNeighborsSubset.subList(0, neighborsSubsetSize));
			System.out.println("Subset of neighbors: " + nodeNeighborsSubset);

			SimpleNode peer = nodeNeighborsSubset.get(rng.nextInt(nodeNeighborsSubset.size()));
			peer = this.graph.getNodeByLabel(peer.getLabel());

			// peer added to choosenPeers for chi-squared test computations
			this.chosenPeers.add(peer.getLabel());
			System.out.println("Chosen peer: " + peer);

			ArrayList<SimpleNode> subsetForPeer = new ArrayList<SimpleNode>();
			for(SimpleNode n : nodeNeighborsSubset) {
				if(n.getLabel() == peer.getLabel()) {
					subsetForPeer.add(node);
				} else {
					subsetForPeer.add(n);
				}
			}
			System.out.println("Subset sent to peer: " + subsetForPeer);
			
			ArrayList<SimpleNode> peerNeighbors = this.graph.getNeighborsOfNode(peer);
			System.out.println("Peer neighbors: " + peerNeighbors);
			int peerNeighborsSubsetSize = Math.min(shuffleLength, peerNeighbors.size());

			if (peerNeighbors.size() > 0) {
				ArrayList<SimpleNode> peerNeighborsSubset = new ArrayList<SimpleNode>();
				peerNeighborsSubset.addAll(peerNeighbors);
				Collections.shuffle(peerNeighborsSubset);
				System.out.println("DEBUG: peerNeighborsSubset: " + peerNeighborsSubset);
				peerNeighborsSubset = new ArrayList<SimpleNode>(peerNeighborsSubset.subList(0, peerNeighborsSubsetSize));
				System.out.println("Subset of peer neighbors: " + peerNeighborsSubset);

				/* 5. Discard entries pointing to P, and entries that are already
				in P’s cache. */
				ArrayList<SimpleEdge> newEdges = new ArrayList<SimpleEdge>();
				ArrayList<SimpleEdge> removedEdges = new ArrayList<SimpleEdge>();
				ArrayList<SimpleNode> nodeKeptEntries = new ArrayList<SimpleNode>();
				for (SimpleNode n : peerNeighborsSubset) {
					n = this.graph.getNodeByLabel(n.getLabel());
					if(this.graph.getEdge(n, node) == null
					&& !nodeNeighbors.contains(n)
					&& n.getLabel() != nodeLabel) {
						if (nodeNeighbors.size() > cacheSize
						&& subsetForPeer.get(subsetForPeer.size()-1) != node) {
							SimpleNode removeTarget = subsetForPeer.get(subsetForPeer.size()-1);
							removeTarget = this.graph.getNodeByLabel(removeTarget.getLabel());
							SimpleEdge toRemove = this.graph.getEdge(node, removeTarget);
							this.graph.removeEdge(toRemove);
							removedEdges.add(toRemove);
						}
						if (nodeNeighbors.size() < cacheSize
						|| subsetForPeer.get(subsetForPeer.size()-1) != node) {
							nodeKeptEntries.add(n);
							this.graph.addEdge(node,n);
							newEdges.add(this.graph.getEdge(node, n));
							nodeNeighbors.add(n);
						}
					}
				}

				System.out.println("Remaining entries within subset from peer: " + nodeKeptEntries);
				ArrayList<SimpleNode> peerKeptEntries = new ArrayList<SimpleNode>();

				/* 6. Update P’s cache to include all remaining entries, by firstly
				using empty cache slots (if any), and secondly replacing entries
				among the ones originally sent to Q. */
				for (SimpleNode n : subsetForPeer) {
					n = this.graph.getNodeByLabel(n.getLabel());
					if (this.graph.getEdge(n, peer) == null
					&& !peerNeighbors.contains(n)
					&& n.getLabel() != peer.getLabel()) {
						if(peerNeighbors.size() > cacheSize) {
							SimpleNode removeTarget = peerNeighborsSubset.get(peerNeighborsSubset.size()-1);
							removeTarget = this.graph.getNodeByLabel(removeTarget.getLabel());
							SimpleEdge toRemove = this.graph.getEdge(peer, removeTarget);
							this.graph.removeEdge(toRemove);
							removedEdges.add(toRemove);
						}
						peerKeptEntries.add(n);
						this.graph.addEdge(peer, n);
						newEdges.add(this.graph.getEdge(peer, n));
						peerNeighbors.add(n);
					}
				}

				System.out.println("Remaining entries within subset from node: " + peerKeptEntries);
				System.out.println("Created Edges: " + newEdges);
				System.out.println("Removed Edges: " + removedEdges);
			}
		}
		System.out.println("Graph after shuffle: " + this.graph);
	}

	@Override
	public ArrayList<Integer> getChosenPeers() {
		return this.chosenPeers;
	}

	@Override
	public AbstractNode nextPeer() { // TODO
		return null;
	}
}

