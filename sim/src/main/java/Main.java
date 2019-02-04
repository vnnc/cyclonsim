import algorithms.*;
import models.*;
import utilities.Utilities;
import org.apache.commons.math3.distribution.ChiSquaredDistribution;

// TODO oh non veut-il qu'on fasse le graphe nous-mêmes ??

// [divers algos] -> interface getNextPeer() -> le khi² qui fait 1000× "appeler getNextPeer() 1000×" et en calcule la moyenne
// en fait osef de la moyenne, il faut l'intervalle de confiance : on s'arrête
// quand (la racine de la variance / nombre de fois qu'on a fait le test) est faible
// où "faible" = alpha qui dépend de la probabilité que ce soit juste, qu'on se fixe

public class Main {
	public static void main(String args[]) {
//		Utilities.info = true;
//		Utilities.debug = true;
		
		AlgorithmCyclonBasic algo = new AlgorithmCyclonBasic();
		Graph g = new Graph(SimpleNode.class, SimpleEdge.class);
		g.importFromCSV("testgraph1.csv");
		final int PEER_AMOUNT = 20;//100000;
		final int SHUFFLE_INTERVAL = 1;
		final int CACHE_SIZE = 4;
		final int SHUFFLE_LENGTH = 2;
		final double CONFIDENCE_LEVEL = 0.90; //0.999 ça va vite mais le test ne passerait jamais
//		g.generateRandom(50, 0.1); //1000, 0.1
//		g.exportToCSV("testgraph0.csv");
		ChiSquaredTest test = new ChiSquaredTest(algo, g);
		boolean reussite = test.runFullTest(0, PEER_AMOUNT, SHUFFLE_INTERVAL, CACHE_SIZE, SHUFFLE_LENGTH, CONFIDENCE_LEVEL);
		Utilities.printInfo("Le test passe-t-il avec succès ? " + reussite);
		Utilities.printInfo(100.0*algo.nbEchanges/(algo.nbEchanges+algo.nbEchecs) + "% de shuffling");


//		AlgorithmRandomReference algo = new AlgorithmRandomReference();
//		Graph g = new Graph(SimpleNode.class,SimpleEdge.class);
//		g.generateRandom(50, 0.2);
//		ChiSquaredTest test = new ChiSquaredTest(algo,g);
//		final int SHUFFLE_INTERVAL = 20;
//		final int PEER_AMOUNT = 12;
//		final int CACHE_SIZE = 3;
//		final int SHUFFLE_LENGTH = 2;
//		final double CONFIDENCE_LEVEL = 0.90;
//		test.runFullTest(0, PEER_AMOUNT, SHUFFLE_INTERVAL, CACHE_SIZE, SHUFFLE_LENGTH, CONFIDENCE_LEVEL);
	}
}


/*

on teste l'équiprobabilité des variables mais pas leur indépendance ????

la fréquence de chaque nœud est quasi la même -> ok, cool, mais osef un petit peu

les fréquences convergent vers l'équiprobabilité mais la différence absolue diverge

int i = 0;
int nextRand() {
	return (i++)%n;
}

-------------------

i=0
r=0
k=0
int nextRand() {
	if (i%k ==0):
		r = rand.nextint(k)
	i++
	return r
}

pour la semaine prochaine : l'intro, le contexte, le(s) problème(s), la problématique/question scientifique

quel(s) test(s) du khi² + set-up expérimental

*/
