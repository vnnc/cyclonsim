import algorithms.*;
import models.*;
import utilities.TestResults;
import utilities.Utilities;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class Main {

	public static void procedureTest1() throws IOException {
		AlgorithmCyclonBasic algo = new AlgorithmCyclonBasic();
		Graph g = new Graph(SimpleNode.class, SimpleEdge.class);
		g.importFromCSV("graphs/testgraph3.csv");
		FileWriter fw = new FileWriter("resultats_shuffle_1.data",false);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write("ID,NOMBRE_SHUFFLE,KHI2_DISTRIBUTION_CALC,KHI2_DISTRIBUTION_THEO,KHI2_INDEP_CALC,KHI2_INDEP_THEO");
		bw.write(System.getProperty("line.separator"));
		final int PEER_AMOUNT = 400;
		int SHUFFLE_INTERVAL;
		final int CACHE_SIZE = 6;
		final int SHUFFLE_LENGTH = 4;
		final double CONFIDENCE_LEVEL = 0.90;
		
		Test test = new Test(algo, g);
		int id = 1;
		
		for(SHUFFLE_INTERVAL=1; SHUFFLE_INTERVAL<=5; SHUFFLE_INTERVAL++) {
			for(int i=0; i<5; i++) {
				TestResults res = test.runFullTest(0, PEER_AMOUNT, SHUFFLE_INTERVAL, CACHE_SIZE, SHUFFLE_LENGTH, CONFIDENCE_LEVEL);
				double shuffleAmount = PEER_AMOUNT * SHUFFLE_INTERVAL;
				String line = id + "," + shuffleAmount + "," + res.getMeanDistrib();
				line = line + "," + res.getExpectedDistrib() + "," + res.getMeanIndep();
				line = line + "," + res.getExpectedIndep();
				bw.write(line);
				bw.write(System.getProperty("line.separator"));
				bw.flush();
				id++;
			}
		}
		bw.close();
		fw.close();
	}
	
	public static void main(String args[]) {
		Utilities.info = true;
		Utilities.debug = false;

		try {
			Main.procedureTest1();
		} catch (IOException e) {
			e.printStackTrace();
		}

//		AlgorithmCyclonBasic algo = new AlgorithmCyclonBasic();
//		Graph g = new Graph(SimpleNode.class, SimpleEdge.class);
//		g.importFromCSV("graphs/testgraph3.csv");
//		final int PEER_AMOUNT = 1000;
//		final int SHUFFLE_INTERVAL = 1;
//		final int CACHE_SIZE = 6;
//		final int SHUFFLE_LENGTH = 4;
//		final double CONFIDENCE_LEVEL = 0.90;
//		Test test = new Test(algo, g);
//		test.runFullTest(0, PEER_AMOUNT, SHUFFLE_INTERVAL, CACHE_SIZE, SHUFFLE_LENGTH, CONFIDENCE_LEVEL);
//		Utilities.printInfo((int) (100.0*algo.nbEchanges/(algo.nbEchanges+algo.nbEchecs)) + "% de shuffling");
//		g.exportToCSV("graphs/output.csv");

//		Graph g = new Graph(SimpleNode.class,SimpleEdge.class);
//		g.generateRandom(20,6);
//		g.exportToCSV("graphs/testgraph3.csv");
//		System.out.println(g);

//		AlgorithmIncrementReference ref = new AlgorithmIncrementReference();
//		Test test = new Test(ref,g);
//		test.runFullTest(0,PEER_AMOUNT,SHUFFLE_INTERVAL,CACHE_SIZE,SHUFFLE_LENGTH,CONFIDENCE_LEVEL);

//		AlgorithmRandomReference ref = new AlgorithmRandomReference();
//		Test test = new Test(ref,g);
//		test.runFullTest(0,PEER_AMOUNT,SHUFFLE_INTERVAL,CACHE_SIZE,SHUFFLE_LENGTH,CONFIDENCE_LEVEL);

	}
}

