import algorithms.*;
import models.*;

import java.util.*;

public class Khi2 {

	private AbstractAlgorithm algo;
	private int graphSize;

	public Khi2 (AbstractAlgorithm algo, int graphSize) {
		this.algo = algo;
		this.graphSize = graphSize;
	}

	public double runTest() {
		double expectedFreq = 1.0/this.graphSize;

		HashMap<Integer,Double> frequencies = new HashMap<Integer, Double>();
		for(Integer occ : this.algo.getChosenPeers()) {
			//System.out.println("Peer seen: "+occ);
			double freqVal = 1.0/this.algo.getChosenPeers().size();
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

		res *= this.algo.getChosenPeers().size();
		return res;
	}
}
