package tests;

import algorithms.AbstractAlgorithm;
import models.*;
import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.TDistribution;
import org.jgrapht.alg.util.Pair;
import utilities.Utilities;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.io.IOException;

public class TestSeries {

	private Graph initialGraph;
	private AbstractAlgorithm algorithm;
	
	private ArrayList<Double> distributionValues = new ArrayList<Double>();
	private ArrayList<Double> correlationValues = new ArrayList<Double>();
	private ArrayList<Double> independenceValues = new ArrayList<Double>();

	public TestSeries(AbstractAlgorithm alg, Graph initialGraph) {
		this.algorithm = alg;
		this.initialGraph = initialGraph;
	}

	public Graph getInitialGraph() {
		return this.initialGraph;
	}

	public void initTestSeries() {
		Utilities.printInfo("Beginning the tests…");

		// Liste contenant les valeurs du test X² calculées
		ArrayList<Double> distributionValues = new ArrayList<Double>();
		ArrayList<Double> correlationValues = new ArrayList<Double>();
		ArrayList<Double> independenceValues = new ArrayList<Double>();
	}
	
	public void runSimpleTest (int sampleSize) {
		ArrayList<Integer> samples = this.algorithm.getTestSample(sampleSize);
		this.correlationValues.add(this.testCorrelation(samples));
		this.distributionValues.add(this.testDistribution(samples));
		this.independenceValues.add(this.testIndependence(samples));
	}
	
	public TestResults endTestSeries(double confidenceLevel) throws IOException {
		int graphSize = this.initialGraph.vertexSet().size();
		ChiSquaredDistribution csd = new ChiSquaredDistribution(graphSize-1);
		ChiSquaredDistribution csdi = new ChiSquaredDistribution(Math.pow(graphSize-1, 2));
		
		// Moyenne des valeurs du test X² calculées
		double chiMeanDistrib = computeMean(this.distributionValues);
		double chiMeanIndep = computeMean(this.independenceValues);
		double correlMean = computeMean(this.correlationValues);

		Utilities.printInfo("Mean value of Pearson Correlation Coefficients: " + correlMean);

		Utilities.printInfo("Mean Χ² statistic for distribution: " + chiMeanDistrib);
		Utilities.printInfo("Maximum Χ² value (Distribution): " +
		                              Collections.max(this.distributionValues));
		Utilities.printInfo("Minimum Χ² value (Distribution): " +
		                              Collections.min(this.distributionValues));

		double criticalValueDistrib = csd.inverseCumulativeProbability(confidenceLevel);
		Utilities.printInfo("Expected critical value for distribution test: " + criticalValueDistrib);

		Utilities.printInfo("Mean Χ² statistic for independence: " + chiMeanIndep);
		Utilities.printInfo("Maximum Χ² value (Independence): " +
		                              Collections.max(this.independenceValues));
		Utilities.printInfo("Minimum Χ² value (Independence): " +
		                              Collections.min(this.independenceValues));

		double criticalValueIndep = csdi.inverseCumulativeProbability(confidenceLevel);
		Utilities.printInfo("Expected critical value for independence test: " + criticalValueIndep);

		return new TestResults(chiMeanDistrib, criticalValueDistrib, chiMeanIndep,
		            criticalValueIndep, independenceValues, distributionValues);
	}

	private double computeMean(ArrayList<Double> values) {
		double sum = 0;
		for(Double value : values) {
			sum += value;
		}
		return sum/values.size();
	}

	private double computeVariance(ArrayList<Double> values) {
		double sumExp = 0;
		double sumExpSquared = 0;

		for(Double value : values) {
			double valueSquared = value*value;
			sumExp += value;
			sumExpSquared += valueSquared;
		}

		double exp = sumExp/values.size();
		double expSquared = sumExpSquared/values.size();
		return (expSquared - exp);
	}

	private double testCorrelation(ArrayList<Integer> samples) {
		ArrayList<Double> X = new ArrayList<Double>();
		ArrayList<Double> Y = new ArrayList<Double>();

		int i = 0;
		while(i <= samples.size()-2) {
			X.add(new Double(samples.get(i)));
			Y.add(new Double(samples.get(i+1)));
			i = i+2;
		}

		double Xm = computeMean(X);
		double Ym = computeMean(Y);

		double sum1 = 0;
		double sum2 = 0;
		double sum3 = 0;
		for(int j=0; j<(samples.size()/2); j++) {
			double Xj = X.get(j);
			double Yj = Y.get(j);
			sum1+= (Xj-Xm)*(Yj-Ym);
			sum2+= Math.pow((Xj-Xm),2);
			sum3+= Math.pow((Yj-Ym),2);
		}

		double coefficient = sum1/(Math.sqrt(sum2)*Math.sqrt(sum3));
		return coefficient;
	}

	private double testDistribution(ArrayList<Integer> samples) {
		Set<SimpleNode> nodeSet = this.initialGraph.vertexSet();
		int graphSize = nodeSet.size();
		double expectedCount = (1.0/(graphSize-1))*samples.size();

		HashMap<Integer, Double> counts = new HashMap<Integer, Double>();

		for(int i=1; i<graphSize; i++) {
			counts.put(i, 0.0);
		}

		for(Integer occ : samples) {
			double prev = counts.get(occ);
			counts.put(occ,prev + 1);
		}

		double res = 0;
		for (Map.Entry<Integer, Double> entry : counts.entrySet()) {
			Utilities.printDebug("(k,v) = (" + entry.getKey() + "," + entry.getValue() + ")");
			res += Math.pow((entry.getValue() - expectedCount), 2) / expectedCount;
		}
		Utilities.printDebug("Computed ChiSquare value (Distribution): "+res);
		return res;
	}

	private double computeSum(ArrayList<Double> values) {
		double sum = 0;
		for(Double value : values) {
			sum+=value;
		}
		return sum;
	}

	private double testIndependence(ArrayList<Integer> samples) {
		int graphSize = this.initialGraph.vertexSet().size();
		
		// Variables aléatoires X et Y pour le test d'independance
		ArrayList<Integer> X = new ArrayList<Integer>();
		ArrayList<Integer> Y = new ArrayList<Integer>();

		// Table de comptage des paires de valeurs (Xi,Yi)
		HashMap<Pair<Integer, Integer>, Integer> counts = new HashMap<Pair<Integer, Integer>, Integer>();

		HashMap<Integer,Integer> XCounts = new HashMap<Integer, Integer>();
		HashMap<Integer,Integer> YCounts = new HashMap<Integer, Integer>();

		int i = 0;
		while(i<=samples.size()-2) {
			Integer newX = samples.get(i);
			Integer newY = samples.get(i+1);

			X.add(newX);
			Y.add(newY);
			i=i+2;

			if (XCounts.containsKey(newX)) {
				Integer prev = XCounts.get(newX);
				XCounts.put(newX,prev+1);
			} else {
				XCounts.put(newX,1);
			}

			if (YCounts.containsKey(newY)) {
				Integer prev = YCounts.get(newY);
				YCounts.put(newY,prev+1);
			} else {
				YCounts.put(newY,1);
			}

			Pair<Integer,Integer> xypair = new Pair<Integer, Integer>(newX,newY);
			if (counts.containsKey(xypair)) {
				Integer prev = counts.get(xypair);
				counts.put(xypair,prev+1);
			} else {
				counts.put(xypair,1);
			}
		}

		double res = 0;
		double expectedCount;
		double pairCount = X.size();

		Pair<Integer,Integer> observedPair;
		double observedCount;
		int observedCountFirst,observedCountSecond;

//		System.out.println("X: "+X);
//		System.out.println("Y: "+Y);
//		System.out.println("Counts: "+counts);

		for(i=1; i<graphSize; i++) {
			for(int j=1; j<graphSize; j++) {

				observedPair = new Pair(i,j);
				if (counts.containsKey(observedPair)) {
					observedCount = counts.get(observedPair);
				} else {
					observedCount = 0;
				}

				if (XCounts.containsKey(observedPair.getFirst())) {
					observedCountFirst = XCounts.get(observedPair.getFirst());
				} else {
					observedCountFirst = 0;
				}

				if (YCounts.containsKey(observedPair.getSecond())) {
					observedCountSecond = YCounts.get(observedPair.getSecond());
				} else {
					observedCountSecond = 0;
				}
				expectedCount = (observedCountFirst*observedCountSecond)/pairCount;

				//System.out.println("Pair: " + observedPair);
				//System.out.println("observed: " + observedCount + " expected: " + expectedCount);
				if (expectedCount!=0) {
					res += Math.pow(observedCount - expectedCount, 2) / expectedCount;
				}
				//System.out.println("Cumul: " + res);
			}
		}

		//System.out.println("Computed ChiSquare value (Independence): "+res);
		return res;
	}

}

