package tests;

import algorithms.AbstractAlgorithm;
import models.*;
import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.TDistribution;
import utilities.Utilities;
import java.util.*;

public class TestsMain {

	private Graph initialGraph;
	private AbstractAlgorithm algorithm;
	private ArrayList<Integer> samples;
	
	// Liste contenant les valeurs du test X² calculées
	private ArrayList<Double> distributionValues = new ArrayList<Double>();
	private ArrayList<Double> correlationValues = new ArrayList<Double>();
	private ArrayList<Double> independenceValues = new ArrayList<Double>();

	// Liste contenant les valeurs d'erreur type des valeurs contenues dans "values" => sqrt(variance/n)
	private ArrayList<Double> standardErrors = new ArrayList<Double>();

	public TestsMain(AbstractAlgorithm alg, Graph initialGraph) {
		this.algorithm = alg;
		this.initialGraph = initialGraph;
	}

	/**
	 * @param nodeLabel label (étiquette) du noeud du graphe qui est observé dans le test
	 * @param peerAmount nombre d'appels de nextPeer par calcul de valeur du
	 * test X² = taille de l'échantillon
	 * @see AbstractAlgorithm#nextPeer(Integer)
	 * @see #testDistribution()
	 * @param shuffleInterval nombre de shuffle sur tout les noeuds du graph entre
	 *                        chaque appel de nextPeer. Exemple: Si shuffleInterval
	 *                        vaut 5, entre chaque appel de nextPeer dans la méthode
	 *                        de calcul du test X² le graph passera par 5 shuffle
	 * @param cacheSize taille maximale de la vue partielle d'un noeud du graphe
	 * @param shuffleLength taille du sous-ensemble de la vue partielle considérée
	 * lors d'un shuffle FIXME le test dépend purement de nextPeer et n'a pas de
	 * le concept de shuffle
	 * @param confidenceLevel niveau de confiance, exemple: si ça vaut 0.98, une
	 *                        valeur qui suit la loi de distribution a 98% de
	 *                        chance de se trouver dans l'intervalle de confiance
	 */
	public boolean runFullTest(int nodeLabel, int peerAmount, int cacheSize,
	                           int shuffleLength, double confidenceLevel) {

		ChiSquaredDistribution csd = new ChiSquaredDistribution(this.initialGraph.vertexSet().size()-1);

		// On utilise la T-Distribution pour obtenir la valeur d'arrêt du test
		TDistribution td = new TDistribution(this.initialGraph.vertexSet().size()-1);
		NormalDistribution nd = new NormalDistribution();

		double alpha = (1-confidenceLevel)/2;
		double limitValue = -td.inverseCumulativeProbability(alpha);

		Utilities.printInfo("Valeur limite: "+limitValue);

		int n = 0;
		do {
			n++;
			this.algorithm.initGraph(initialGraph, cacheSize, shuffleLength);
			this.genPeerSamples(nodeLabel, peerAmount);
			distributionValues.add(testDistribution()); // pas de threads pour lui, mais une classe spéciale quand même
			
			TestCorrelation tc = new TestCorrelation(this.samples, this.correlationValues);
			Thread tr1 = new Thread(tc);
			tr1.start();
			
			TestIndependance ti = new TestIndependance(this.samples, this.independenceValues);
			Thread tr2 = new Thread(ti);
			tr2.start();
			
			double err = computeVariance(distributionValues)/n;
			if (err < 0.000001) {
				err = 0.0;
			}
			standardErrors.add(Math.sqrt(err));
			Utilities.printInfo("Computed standard error: " + standardErrors.get(n-1));
			//Utilities.printInfo("Coefficient d'indépendance : " + independenceValues.get(n-1));
//		} while(standardErrors.get(n-1) > limitValue);
		} while (n < 100);

		// Moyenne des valeurs du test X² calculées
		double chiMeanDistrib = computeMean(distributionValues);
		double chiMeanIndep = computeMean(independenceValues);
		double correlMean = computeMean(correlationValues);

		Utilities.printInfo("Mean value of Pearson Correlation Coefficients: "+correlMean);
		Utilities.printInfo("Mean ChiSquared statistic for distribution: " + chiMeanDistrib);
		Utilities.printInfo("Mean ChiSquared statistic for independence: " + chiMeanIndep);
		Utilities.printInfo("Maximum ChiSquared value (Distribution): " + Collections.max(distributionValues));
		Utilities.printInfo("Minimum ChiSquared value (Distribution): " + Collections.min(distributionValues));

		// Borne supérieure de l'intervalle de confiance selon les valeurs calculées
		double rightBound = chiMeanDistrib + limitValue*standardErrors.get(n-1);

		// Borne inférieure de l'intervalle de confiance selon les valeurs calculées
		double leftBound = chiMeanDistrib - limitValue*standardErrors.get(n-1);

		// Valeur critique selon la distribution X²
		double criticalValue = csd.inverseCumulativeProbability(confidenceLevel);

		Utilities.printInfo("Expected critical value for ChiSquared distribution: " + criticalValue);
		Utilities.printInfo("Computed confidence interval: "
		                    + "[" + leftBound + ", " + rightBound + "]");

		boolean distribTest;
		if(chiMeanDistrib < criticalValue){
			distribTest = true;
		}else{
			distribTest = false;
		}
		Utilities.printInfo("Maximum ChiSquared value (Independence): " + Collections.max(independenceValues));
		Utilities.printInfo("Minimum ChiSquared value (Independence): " + Collections.min(independenceValues));

		boolean indepTest;
		if(chiMeanIndep < criticalValue){
			indepTest = true;
		}else{
			indepTest = false;
		}
		return (distribTest && indepTest);
	}

	public static double computeMean(ArrayList<Double> values){
		double sum = 0;
		for(Double value : values) {
			sum+=value;
		}
		return sum/values.size();
	}

	public static double computeVariance(ArrayList<Double> values){
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

	private void genPeerSamples(int nodeLabel ,int peerAmount){
		ArrayList<Integer> chosenPeers = new ArrayList<Integer>();
		for(int i=0; i<peerAmount; i++) {
			AbstractNode nextPeer = this.algorithm.nextPeer(nodeLabel);
			chosenPeers.add(nextPeer.getLabel());
		}
		this.samples = chosenPeers;
	}

	private double testDistribution() {
		Set<SimpleNode> nodeSet = this.initialGraph.vertexSet();
		int graphSize = nodeSet.size();
		double expectedFreq = 1.0/graphSize;

		HashMap<Integer, Double> frequencies = new HashMap<Integer, Double>();
		for(Integer occ : this.samples) {
			double freqVal = 1.0/this.samples.size();
			if (frequencies.containsKey(occ)) {
				double prev = frequencies.get(occ);
				frequencies.put(occ,prev + freqVal);
			} else {
				frequencies.put(occ, freqVal);
			}
		}

		double res = 0;
		for (Map.Entry<Integer, Double> entry : frequencies.entrySet()) {
			Utilities.printDebug("(k,v) = (" + entry.getKey() + "," + entry.getValue() + ")");
			res += Math.pow((entry.getValue() - expectedFreq), 2)/expectedFreq;
		}
		res *= this.samples.size();
		Utilities.printDebug("Computed ChiSquare value (Distribution): "+res);
		return res;
	}

	private double computeSum(ArrayList<Double> values){
		// n'est utilisée nulle part
		double sum = 0;
		for(Double value : values) {
			sum+=value;
		}
		return sum;
	}

}

