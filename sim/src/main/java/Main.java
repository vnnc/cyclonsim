import algorithms.*;
import models.*;
import org.apache.commons.math3.distribution.ChiSquaredDistribution;

// TODO oh non il veut qu'on fasse le graphe nous-mêmes ??

// TODO faire le test plein de fois pour avoir des résultats statistiquement valables

// [divers algos] -> interface getNextPeer() -> le khi² qui fait 1000× "appeler getNextPeer() 1000×" et en calcule la moyenne
// en fait osef de la moyenne, il faut l'intervalle de confiance : on arrête quand (la racine de la variance / nombre de fois qu'on a fait le test) est faible
// faible = alpha qui dépend de la probabilité que ce soit juste, qu'on se fixe

public class Main {


	public static void main(String args[]) {

		AlgorithmBasic algo = new AlgorithmBasic();
		Graph g = new Graph(SimpleNode.class,SimpleEdge.class);
		g.importFromCSV("testgraph1.csv");
		final int CACHE_SIZE = 3;
		final int SHUFFLE_LENGTH = 2;

		ChiSquaredTest test = new ChiSquaredTest(algo,g);

		test.runFullTest(0,150,20,CACHE_SIZE,SHUFFLE_LENGTH,0.98);

	}
}
