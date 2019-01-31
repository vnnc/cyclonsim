import algorithms.*;
import models.*;

// TODO oh non il veut qu'on fasse le graphe nous-mêmes ??

// TODO faire le test plein de fois pour avoir des résultats statistiquement valables

// [divers algos] -> interface getNextPeer() -> le khi² qui fait 1000× "appeler getNextPeer() 1000×" et en calcule la moyenne
// en fait osef de la moyenne, il faut l'intervalle de confiance : on arrête quand (la racine de la variance / nombre de fois qu'on a fait le test) est faible
// faible = alpha qui dépend de la probabilité que ce soit juste, qu'on se fixe

public class Main {

	public static void main(String args[]) {
		/*AlgorithmBasic alg = new AlgorithmBasic();
		final int GRAPH_SIZE = 10;
		final int CACHE_SIZE = 3;
		
		alg.initRandomGraph(GRAPH_SIZE,CACHE_SIZE);
		alg.round();
		System.out.println("Choosen peers: " + alg.getChosenPeers());
		Khi2 khi2 = new Khi2(alg, GRAPH_SIZE);
		System.out.println("ChiSquared value: " + khi2.runTest());*/
		Graph g = new Graph(SimpleNode.class,SimpleEdge.class);
		g.importFromCSV("testgraph1.csv");
		System.out.println(g);
	}
}
