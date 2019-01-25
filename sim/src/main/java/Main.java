import algorithms.*;
import models.*;

// TODO oh non il veut qu'on fasse le graphe nous-mêmes ??

// TODO faire le test plein de fois pour avoir des résultats statistiquement valables

// [divers algos] -> interface getNextPeer() -> le khi² qui fait 1000× "appeler getNextPeer() 1000×" et en calcule la moyenne
// en fait osef de la moyenne, cf intervalle de confiance : on arrête quand (la racine de la variance / nombre de fois qu'on a fait le test) est faible
// faible = alpha qui dépend de la probabilité que ce soit juste qu'on se fixe

public class Main {

	public static void structureTest() {
		/*System.out.println("Running");
		Graph<NodeTest> G = new Graph<NodeTest>(NodeTest.class);

		G.generateRandom(5,8);

		System.out.println("Age of node 0: "+G.getNodeByLabel(0).getAge());
		System.out.println("Neighbors of node 0: "+G.getNeighborsOfNode(G.getNodeByLabel(0)));
		System.out.println(G);*/
	}

	public static void main(String args[]) {
		AlgorithmBasic alg = new AlgorithmBasic();
		final int GRAPH_SIZE = 10;
		
		alg.initGraph(GRAPH_SIZE);
		alg.round();
		System.out.println("Choosen peers: " + alg.getChosenPeers());
		Khi2 khi2 = new Khi2(alg, GRAPH_SIZE);
		System.out.println("ChiSquared value: " + khi2.runTest());
	}
}
