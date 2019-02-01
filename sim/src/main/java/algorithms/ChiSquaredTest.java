package algorithms;

import models.*;
import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;
import utilities.Utilities;
import java.util.*;

public class ChiSquaredTest {

	private Graph initialGraph;
	private AbstractAlgorithm algorithm;

	public ChiSquaredTest(AbstractAlgorithm alg,Graph initialGraph) {
		this.algorithm = alg;
		this.initialGraph = initialGraph;
	}


	/**
	 *
	 * @param nodeLabel label (étiquette) du noeud du graph qui est observé dans le test
	 * @param peerAmount nombre d'appels de nextPeer par calcul de valeur du test X²
	 * @see AbstractAlgorithm#nextPeer(Object)
	 * @see #runComputation(int, int, int)
	 * @param shuffleInterval nombre de shuffle sur tout les noeuds du graph entre chaque appel de nextPeer
	 *                        exemple: Si shuffleInterval vaut 5, entre chaque appel de nextPeer dans la méthode de calcul
	 *                        du test X² le graph passera par 5 shuffle
	 * @param cacheSize taille maximale de la vue partielle d'un noeud du graph
	 * @param shuffleLength taille de la partie de la vue partielle considérée lors d'un shuffle
	 * @param confidenceLevel niveau de confiance, exemple: si vaut 0.98 , une valeur qui suit la loi de distribution a 98% de chance
	 *                        de se trouver dans l'intervalle de confiance
	 */
	public void runFullTest(int nodeLabel,int peerAmount,int shuffleInterval,int cacheSize,int shuffleLength,double confidenceLevel)
	{
		// Liste contenant les valeurs du test X² calculées
		ArrayList<Double> values = new ArrayList<Double>();

		// Liste contenant les valeurs de déviation des valeurs contenues dans "values" => sqrt(variance/n)
		ArrayList<Double> deviations = new ArrayList<Double>();

		int n = 0;
		do{
			n++;
			this.algorithm.initGraph(initialGraph,cacheSize,shuffleLength);
			values.add(runComputation(nodeLabel,peerAmount,shuffleInterval));
			deviations.add(Math.sqrt(computeVariance(values)/n));
			Utilities.printInfo("Computed deviation: "+deviations.get(n-1));
		}while(deviations.get(n-1) > confidenceLevel);

		// Moyenne des valeurs du test X² calculées
		double chiMean = computeMean(values);

		Utilities.printInfo("Mean ChiSquared value: "+chiMean);

		Utilities.printInfo("Maximum ChiSquared value: "+Collections.max(values));

		Utilities.printInfo("Minimum ChiSquared value: "+Collections.min(values));

		ChiSquaredDistribution csd = new ChiSquaredDistribution(this.initialGraph.vertexSet().size()-1);

		// Valeur critique selon la distribution X²
		double criticalValue = csd.inverseCumulativeProbability(confidenceLevel);

		// Borne supérieure de l'intervalle de confiance
		double rightBound = csd.inverseCumulativeProbability(1-((1-confidenceLevel)/2));

		// Borne inférieure de l'intervalle de confiance
		double leftBound = csd.inverseCumulativeProbability((1-confidenceLevel)/2);

		Utilities.printInfo("Critical value of ChiSquared distribution : "+criticalValue);

		Utilities.printInfo( "Confidence interval: "+"["+leftBound+" , "+rightBound+"]");
	}

	private double computeMean(ArrayList<Double> values){
		double sum = 0;
		for(Double value : values){
			sum+=value;
		}
		return sum/values.size();
	}

	private double computeVariance(ArrayList<Double> values){

		double sumExp = 0;
		double sumExpSquared = 0;

		for(Double value : values){
			double valueSquared = value*value;
			sumExp += value;
			sumExpSquared += valueSquared;
		}

		double exp = sumExp/values.size();
		double expSquared = sumExpSquared/values.size();

		return (expSquared - exp);
	}

	private double runComputation(int nodeLabel,int peerAmount,int shuffleInterval) {

		int graphSize = this.algorithm.getGraph().vertexSet().size();

		double expectedFreq = 1.0/graphSize;

		ArrayList<Integer> chosenPeers = new ArrayList<Integer>();

		for(int i=0;i<peerAmount;i++)
		{
			this.algorithm.multiShuffleAll(shuffleInterval);
			chosenPeers.add(this.algorithm.nextPeer(this.algorithm.getGraph().getNodeByLabel(nodeLabel)).getLabel());
		}

		HashMap<Integer,Double> frequencies = new HashMap<Integer, Double>();
		for(Integer occ : chosenPeers) {
			double freqVal = 1.0/chosenPeers.size();
			if (frequencies.containsKey(occ)) {
				double prev = frequencies.get(occ);
				frequencies.put(occ,prev + freqVal);
			} else {
				frequencies.put(occ, freqVal);
			}
		}

		double res = 0;

		for (Map.Entry<Integer, Double> entry : frequencies.entrySet()) {
			Utilities.printDebug("(k,v) = ("+entry.getKey()+","+entry.getValue()+")");
			res+= Math.pow((entry.getValue() - expectedFreq),2)/expectedFreq;
		}

		res *= chosenPeers.size();
		return res;
	}
}
