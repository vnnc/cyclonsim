//la package algorithms c'est pour les nombreuses variantes de cyclon qu'on va
//faire, mais le test du chi² est entièrement indépendant de cela et ne rentre
//donc pas dans ce package. À la limite il peut aller dans utilities ?

import algorithms.AbstractAlgorithm;
import models.*;
import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.TDistribution;
import org.jgrapht.alg.util.Pair;
import utilities.TestResults;
import utilities.Utilities;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class Test {

	private Graph initialGraph;
	private AbstractAlgorithm algorithm;
	private ArrayList<Integer> samples;

	public Test(AbstractAlgorithm alg, Graph initialGraph) {
		this.algorithm = alg;
		this.initialGraph = initialGraph;
	}

	/**
	 * @param nodeLabel label (étiquette) du nœud du graphe qui est observé dans le test
	 * @param peerAmount nombre d'appels de nextPeer par calcul de valeur du test X²
	 * @see AbstractAlgorithm#nextPeer(Integer)
	 * @see #testDistribution()
	 * @param shuffleInterval nombre de shuffle sur tout les nœuds du graphe entre
	 *                        chaque appel de nextPeer. Exemple: si shuffleInterval
	 *                        vaut 5, entre chaque appel de nextPeer dans la méthode
	 *                        de calcul du test X² le graph passera par 5 shuffle
	 * @param cacheSize taille maximale de la vue partielle d'un nœud du graphe
	 * @param shuffleLength taille du sous-ensemble de la vue partielle considérée lors d'un shuffle
	 * @param confidenceLevel niveau de confiance, exemple: si ça vaut 0.98, une
	 *                        valeur qui suit la loi de distribution a 98% de
	 *                        chance de se trouver dans l'intervalle de confiance
	 */
	public TestResults runFullTest(int nodeLabel, int peerAmount, int shuffleInterval,
	                               int cacheSize, int shuffleLength, double confidenceLevel) {

		int graphSize = this.initialGraph.vertexSet().size();
		// Liste contenant les valeurs du test X² calculées
		ArrayList<Double> distributionValues = new ArrayList<Double>();
		ArrayList<Double> correlationValues = new ArrayList<Double>();
		ArrayList<Double> independenceValues = new ArrayList<Double>();

		// Liste contenant les valeurs d'erreur type des valeurs contenues dans "values" => sqrt(variance/n)
		ArrayList<Double> standardErrors = new ArrayList<Double>();

		ChiSquaredDistribution csd = new ChiSquaredDistribution(graphSize-1);
		ChiSquaredDistribution csdi = new ChiSquaredDistribution(Math.pow(graphSize,2));
		int n = 0;
		do {
			n++;
			this.algorithm.initGraph((Graph) initialGraph.clone(), cacheSize, shuffleLength);
			this.genPeerSamples(nodeLabel,peerAmount,shuffleInterval);
			correlationValues.add(testCorrelation()); //TODO threads
			distributionValues.add(testDistribution()); //TODO threads
			independenceValues.add(testIndependence()); //TODO threads;
			double err = computeVariance(distributionValues)/n;
			if (err < 0.000001) {
				err = 0.0;
			}
			standardErrors.add(Math.sqrt(err));
			Utilities.printInfo("Computed standard error: " + standardErrors.get(n-1));
			//Utilities.printInfo("Coefficient d'indépendance : " + independenceValues.get(n-1));
		} while(distributionValues.size() < 50);

		// Moyenne des valeurs du test X² calculées
		double chiMeanDistrib = computeMean(distributionValues);
		double chiMeanIndep = computeMean(independenceValues);
		double correlMean = computeMean(correlationValues);

		Utilities.printInfo("Mean value of Pearson Correlation Coefficients: "+correlMean);

		Utilities.printInfo("Mean ChiSquared statistic for distribution: " + chiMeanDistrib);
		Utilities.printInfo("Maximum ChiSquared value (Distribution): " + Collections.max(distributionValues));
		Utilities.printInfo("Minimum ChiSquared value (Distribution): " + Collections.min(distributionValues));

		double criticalValueDistrib = csd.inverseCumulativeProbability(confidenceLevel);
		Utilities.printInfo("Expected critical value for distribution test: " + criticalValueDistrib);

		Utilities.printInfo("Mean ChiSquared statistic for independence: " + chiMeanIndep);
		Utilities.printInfo("Maximum ChiSquared value (Independence): " + Collections.max(independenceValues));
		Utilities.printInfo("Minimum ChiSquared value (Independence): " + Collections.min(independenceValues));

		double criticalValueIndep = csdi.inverseCumulativeProbability(confidenceLevel);
		Utilities.printInfo("Expected critical value for independence test: " + criticalValueIndep);

		return new TestResults(chiMeanDistrib, criticalValueDistrib, chiMeanIndep, criticalValueIndep);
	}

	private double computeMean(ArrayList<Double> values){
		double sum = 0;
		for(Double value : values) {
			sum+=value;
		}
		return sum/values.size();
	}

	private double computeVariance(ArrayList<Double> values){
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

	private void genPeerSamples(int nodeLabel,int peerAmount,int shuffleInterval){
		ArrayList<Integer> chosenPeers = new ArrayList<Integer>();
		for(int i=0; i<peerAmount; i++) {
			this.algorithm.multiShuffleAll(shuffleInterval);
			AbstractNode nextPeer = this.algorithm.nextPeer(nodeLabel);
			chosenPeers.add(nextPeer.getLabel());
		}
		this.samples = chosenPeers;
	}

	private double testCorrelation(){

		ArrayList<Double> X = new ArrayList<Double>();
		ArrayList<Double> Y = new ArrayList<Double>();

		int i = 0;
		while(i <= this.samples.size()-2){
			X.add(new Double(this.samples.get(i)));
			Y.add(new Double(this.samples.get(i+1)));
			i = i+2;
		}



		double Xm = computeMean(X);
		double Ym = computeMean(Y);

		double sum1 = 0;
		double sum2 = 0;
		double sum3 = 0;
		for(int j=0;j<(this.samples.size()/2);j++){
			double Xj = X.get(j);
			double Yj = Y.get(j);
			sum1+= (Xj-Xm)*(Yj-Ym);
			sum2+= Math.pow((Xj-Xm),2);
			sum3+= Math.pow((Yj-Ym),2);
		}

		double coefficient = sum1/(Math.sqrt(sum2)*Math.sqrt(sum3));
		return coefficient;
	}

	private double testDistribution() {
		Set<SimpleNode> nodeSet = this.initialGraph.vertexSet();
		int graphSize = nodeSet.size();
		double expectedCount = (1.0/(graphSize-1))*this.samples.size();


		HashMap<Integer, Double> counts = new HashMap<Integer, Double>();

		for(int i=1; i<graphSize; i++){
			counts.put(i, 0.0);
		}

		for(Integer occ : this.samples) {
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

	private double computeSum(ArrayList<Double> values){

		double sum = 0;
		for(Double value : values) {
			sum+=value;
		}
		return sum;
	}
	private double testIndependence(){
		int graphSize = this.initialGraph.vertexSet().size();
		
		// Variables aléatoires X et Y pour le test d'independance
		ArrayList<Integer> X = new ArrayList<Integer>();
		ArrayList<Integer> Y = new ArrayList<Integer>();

		// Table de comptage des paires de valeurs (Xi,Yi)
		HashMap<Pair<Integer, Integer>, Integer> counts = new HashMap<Pair<Integer, Integer>, Integer>();

		HashMap<Integer,Integer> XCounts = new HashMap<Integer, Integer>();
		HashMap<Integer,Integer> YCounts = new HashMap<Integer, Integer>();

		int i = 0;
		while(i<=this.samples.size()-2){
			Integer newX = this.samples.get(i);
			Integer newY = this.samples.get(i+1);

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

		for(i=1; i<graphSize; i++){
			for(int j=1; j<graphSize; j++){

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

