import algorithms.*;
import models.*;

import java.util.*;

public class ChiSquaredTest {

	private Graph graph;
	private AbstractAlgorithm algorithm;

	public ChiSquaredTest(AbstractAlgorithm alg) {
		this.algorithm = alg;
		this.graph = this.algorithm.getGraph();
	}


	public double runTest(int nodeLabel,int peerAmount,int shuffleInterval) {

		int graphSize = this.graph.vertexSet().size();

		double expectedFreq = 1.0/graphSize;

		ArrayList<Integer> chosenPeers = new ArrayList<Integer>();

		for(int i=0;i<peerAmount;i++)
		{
			this.algorithm.multiShuffleAll(shuffleInterval);
			chosenPeers.add(this.algorithm.nextPeer(this.graph.getNodeByLabel(nodeLabel)).getLabel());
		}

		HashMap<Integer,Double> frequencies = new HashMap<Integer, Double>();
		for(Integer occ : chosenPeers) {
			//System.out.println("Peer seen: "+occ);
			double freqVal = 1.0/chosenPeers.size();
			if (frequencies.containsKey(occ)) {
				//System.out.println("Peer in table");
				double prev = frequencies.get(occ);
				//System.out.println("Previous value: "+prev);
				frequencies.put(occ,prev + freqVal);
			} else {
				frequencies.put(occ, freqVal);
			}
		}

		double res = 0;

		for (Map.Entry<Integer, Double> entry : frequencies.entrySet()) {
			System.out.println("(k,v) = ("+entry.getKey()+","+entry.getValue()+")");
			res+= Math.pow((entry.getValue() - expectedFreq),2)/expectedFreq;
			//System.out.println("entry.getValue() - expectedFreq: "+(entry.getValue() - expectedFreq));
			//System.out.println("(entry.getValue() - expectedFreq)²: "+Math.pow(entry.getValue() - expectedFreq,2));
			//System.out.println("Math.pow((entry.getValue() - expectedFreq),2)/expectedFreq : "+(Math.pow((entry.getValue() - expectedFreq),2)/expectedFreq));
		}

		res *= chosenPeers.size();
		return res;
	}
}
