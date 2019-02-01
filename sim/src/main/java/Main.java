import algorithms.*;
import models.*;
//import ChiSquaredTest;
import org.apache.commons.math3.distribution.ChiSquaredDistribution;

// TODO oh non veut-il qu'on fasse le graphe nous-mêmes ??

// [divers algos] -> interface getNextPeer() -> le khi² qui fait 1000× "appeler getNextPeer() 1000×" et en calcule la moyenne
// en fait osef de la moyenne, il faut l'intervalle de confiance : on s'arrête
// quand (la racine de la variance / nombre de fois qu'on a fait le test) est faible
// où "faible" = alpha qui dépend de la probabilité que ce soit juste, qu'on se fixe

public class Main {
	public static void main(String args[]) {
		AlgorithmBasic algo = new AlgorithmBasic();
		Graph g = new Graph(SimpleNode.class,SimpleEdge.class);
		g.importFromCSV("testgraph1.csv");
		final int SHUFFLE_INTERVAL = 150;
		final int PEER_AMOUNT = 20;
		final int CACHE_SIZE = 3;
		final int SHUFFLE_LENGTH = 2;
		final double CONFIDENCE_LEVEL = 0.98;
		ChiSquaredTest test = new ChiSquaredTest(algo, g);
		test.runFullTest(0, SHUFFLE_INTERVAL, PEER_AMOUNT, CACHE_SIZE, SHUFFLE_LENGTH, CONFIDENCE_LEVEL);
	}
}
