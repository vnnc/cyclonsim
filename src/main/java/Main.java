import algorithms.*;
import models.*;
import tests.TestsMain;
import utilities.Utilities;

import java.lang.reflect.InvocationTargetException;

public class Main {
	public static void main(String args[])
	throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

		Utilities.info = true;
		Utilities.debug = false;

		AlgorithmCyclonBasic algo = new AlgorithmCyclonBasic(); // TODO donner les paramètres à l'algo plutôt qu'aux tests
		Graph g = new Graph(SimpleNode.class, SimpleEdge.class);
		g.importFromCSV("graphs/testgraph3.csv");
		final int PEER_AMOUNT = 200;
		final int CACHE_SIZE = 6;
		final int SHUFFLE_LENGTH = 4;
		final double CONFIDENCE_LEVEL = 0.90;
		TestsMain test = new TestsMain(algo, g);
		boolean reussite = test.runFullTest(0, PEER_AMOUNT, CACHE_SIZE, SHUFFLE_LENGTH, CONFIDENCE_LEVEL);
		Utilities.printInfo("Le test passe-t-il avec succès ? " + reussite);
		Utilities.printInfo((int) (100.0*algo.nbEchanges/(algo.nbEchanges+algo.nbEchecs)) + "% de shuffling");
		g.exportToCSV("graphs/output.csv");

//		Graph g = new Graph(SimpleNode.class,SimpleEdge.class);
//		g.generateRandom(20,6);
//		g.exportToCSV("graphs/testgraph3.csv");
//		System.out.println(g);

	}
}


