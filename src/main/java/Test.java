//la package algorithms c'est pour les nombreuses variantes de cyclon qu'on va
//faire, mais le test du chi² est entièrement indépendant de cela et ne rentre
//donc pas dans ce package. À la limite il peut aller dans utilities ?

import algorithms.AbstractAlgorithm;
import models.*;
import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.TDistribution;
import org.jgrapht.alg.util.Pair;
import utilities.Utilities;
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
	 * @param nodeLabel label (étiquette) du noeud du graphe qui est observé dans le test
	 * @param peerAmount nombre d'appels de nextPeer par calcul de valeur du test X²
	 * @see AbstractAlgorithm#nextPeer(Integer)
	 * @see #testDistribution()
	 * @param shuffleInterval nombre de shuffle sur tout les noeuds du graph entre
	 *                        chaque appel de nextPeer. Exemple: Si shuffleInterval
	 *                        vaut 5, entre chaque appel de nextPeer dans la méthode
	 *                        de calcul du test X² le graph passera par 5 shuffle
	 * @param cacheSize taille maximale de la vue partielle d'un noeud du graphe
	 * @param shuffleLength taille du sous-ensemble de la vue partielle considérée lors d'un shuffle
	 * @param confidenceLevel niveau de confiance, exemple: si ça vaut 0.98, une
	 *                        valeur qui suit la loi de distribution a 98% de
	 *                        chance de se trouver dans l'intervalle de confiance
	 */
	public boolean runFullTest(int nodeLabel, int peerAmount, int shuffleInterval,
	                        int cacheSize, int shuffleLength, double confidenceLevel) {
		// Liste contenant les valeurs du test X² calculées
		ArrayList<Double> distributionValues = new ArrayList<Double>();
		ArrayList<Double> correlationValues = new ArrayList<Double>();
		ArrayList<Double> independenceValues = new ArrayList<Double>();

		// Liste contenant les valeurs d'erreur type des valeurs contenues dans "values" => sqrt(variance/n)
		ArrayList<Double> standardErrors = new ArrayList<Double>();

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
		} while(distributionValues.size()<100);

		System.out.println("independence statistics: "+independenceValues);
		System.out.println("taille de la :"+distributionValues.size());
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
		while(i<=this.samples.size()-2){
			X.add(new Double(this.samples.get(i)));
			Y.add(new Double(this.samples.get(i+1)));
			i=i+2;
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

		double sum = 0;
		for(Double value : values) {
			sum+=value;
		}
		return sum;
	}
	private double testIndependence(){
		ArrayList<Integer> X = new ArrayList<Integer>();
		ArrayList<Integer> Y = new ArrayList<Integer>();
		HashMap<Pair<Integer,Integer>,Integer> counts = new HashMap<Pair<Integer, Integer>, Integer>();

		int i = 0;
		while(i<=this.samples.size()-2){
			Integer newX = this.samples.get(i);
			Integer newY = this.samples.get(i+1);

			X.add(newX);
			Y.add(newY);
			i=i+2;

			Pair<Integer,Integer> xypair = new Pair<Integer, Integer>(newX,newY);
			if(counts.containsKey(xypair)){
				Integer prev = counts.get(xypair);
				counts.put(xypair,prev+1);
			}else{
				counts.put(xypair,1);
			}
		}

		System.out.println("X: "+X);
		System.out.println("Y: "+Y);
		System.out.println("Counts: "+counts);


		double res = 0;
		double expectedFreq = 1/Math.pow(this.initialGraph.vertexSet().size(),2);
		double pairCount = X.size();

		for(Map.Entry<Pair<Integer,Integer>,Integer> entry : counts.entrySet()){

			double observed = entry.getValue();
			double observedFreq = observed/pairCount;
			res += Math.pow(observedFreq - expectedFreq, 2)/expectedFreq;
			System.out.println("Pair: "+entry.getKey());
			System.out.println("observedFreq: "+observedFreq);
		}

		Utilities.printDebug("Computed ChiSquare value (Independence): "+res);
		return res;
	}

}

