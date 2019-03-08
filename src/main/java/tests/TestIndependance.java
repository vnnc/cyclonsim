package tests;

import algorithms.AbstractAlgorithm;
import models.*;
import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.TDistribution;
import utilities.Utilities;
import java.util.*;

public class TestIndependance implements Runnable {

	private ArrayList<Integer> samples;
	private ArrayList<Double> values;

	public TestIndependance(ArrayList<Integer> samples, ArrayList<Double> values) {
		this.samples = samples;
		this.values = values;
	}

	public void run(){
		ArrayList<Integer> X = new ArrayList<Integer>();
		ArrayList<Integer> Y = new ArrayList<Integer>();

		int i = 0;
		while(i<=this.samples.size()-2){
			X.add(new Integer(this.samples.get(i)));
			Y.add(new Integer(this.samples.get(i+1)));
			i=i+2;
		}
		HashMap<Integer,Integer> XCounts = new HashMap<Integer, Integer>();
		HashMap<Integer,Integer> YCounts = new HashMap<Integer, Integer>();

		for(Integer occ : X){
			if(XCounts.containsKey(occ)){
				int prev = XCounts.get(occ);
				XCounts.put(occ,prev+1);
			}else{
				XCounts.put(occ,1);
			}
		}

		for(Integer occ : Y){
			if(YCounts.containsKey(occ)){
				int prev = YCounts.get(occ);
				YCounts.put(occ,prev+1);
			}else{
				YCounts.put(occ,1);
			}
		}

		HashMap<Integer,Double> ExpectedCounts = new HashMap<Integer, Double>();

		double res = 0;
		double expectedFreq;
		int XCountCell;
		int YCountCell;
		for(Map.Entry<Integer,Integer> entry : XCounts.entrySet()){
			XCountCell = (XCounts.containsKey(entry.getKey())) ? XCounts.get(entry.getKey()) : 0;
			YCountCell = (YCounts.containsKey(entry.getKey())) ? YCounts.get(entry.getKey()) : 0;
			expectedFreq = (XCountCell + YCountCell)/2.0;
			res += Math.pow((entry.getValue() - expectedFreq), 2)/expectedFreq;
		}

		for(Map.Entry<Integer,Integer> entry : YCounts.entrySet()){
			XCountCell = (XCounts.containsKey(entry.getKey())) ? XCounts.get(entry.getKey()) : 0;
			YCountCell = (YCounts.containsKey(entry.getKey())) ? YCounts.get(entry.getKey()) : 0;
			expectedFreq = (XCountCell + YCountCell)/2.0;
			res += Math.pow((entry.getValue() - expectedFreq), 2)/expectedFreq;
		}

		Utilities.printDebug("Computed ChiSquare value (Independence): "+res);
		this.values.add(res);
	}

}
