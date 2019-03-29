import algorithms.*;
import models.*;
import tests.*;
import utilities.Utilities;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class Main {

	public static void procedureTestCyclon() throws IOException {
		AlgorithmCyclonBasic algo = new AlgorithmCyclonBasic();
		Graph g = new Graph(SimpleNode.class, SimpleEdge.class);
		g.importFromCSV("graphs/testgraph4.csv");

		final String FILE_NUMBER = "1";
		FileWriter fw = new FileWriter("statistical_data/resultats_shuffle_"+FILE_NUMBER+".csv", false);
		FileWriter fw2 = new FileWriter("statistical_data/annexe_resultats_"+FILE_NUMBER+".csv", false);

		BufferedWriter bw = new BufferedWriter(fw);
		bw.write("ID,SHUFFLE_INTERVAL,KHI2_DISTRIB_THEO,KHI2_DISTRIB_SUCC,KHI2_INDEP_THEO,KHI2_INDEP_SUCC");
		bw.write(System.getProperty("line.separator"));

		BufferedWriter bw2 = new BufferedWriter(fw2);
		bw2.write("SHUFFLE_INTERVAL,DIST_VALUE,INDEP_VALUE");
		bw2.write(System.getProperty("line.separator"));

		final int SAMPLE_SIZE = 20;
		int SHUFFLE_INTERVAL;
		final int CACHE_SIZE = 3;
		final int SHUFFLE_LENGTH = 2;
		final double CONFIDENCE_LEVEL = 0.90;

		TestSeries test = new TestSeries(algo, g);
		int id = 1;

		final int MAX_INTERVAL = 5;

		// TODO parallélisable
		for(SHUFFLE_INTERVAL=1; SHUFFLE_INTERVAL<=MAX_INTERVAL; SHUFFLE_INTERVAL++) {
			test.initTestSeries();
			algo.setInterval(SHUFFLE_INTERVAL);
			for (int i=0; i<50; i++) {
				algo.initGraph((Graph) test.getInitialGraph().clone(), CACHE_SIZE, SHUFFLE_LENGTH);
				test.runSimpleTest(SAMPLE_SIZE);
				Utilities.printInfo(i+1 + "/50");
			}
			TestResults res = test.endTestSeries(CONFIDENCE_LEVEL);

			String line = id + "," + SHUFFLE_INTERVAL + "," + res.getString();
			bw.write(line);
			bw.write(System.getProperty("line.separator"));
			bw.flush();

			ArrayList<Double> distributionValues = res.getDistribValues();
			ArrayList<Double> independenceValues = res.getIndepValues();

			for(int k=0; k<distributionValues.size(); k++){
				line = id + "," + distributionValues.get(k) + "," + independenceValues.get(k);
				bw2.write(line);
				bw2.write(System.getProperty("line.separator"));
				bw2.flush();
			}
			id++;
		}

		bw.close();
		bw2.close();
		fw.close();
		fw2.close();
	}

	public static void main(String args[]) throws InvocationTargetException,
	     NoSuchMethodException, InstantiationException, IllegalAccessException {
		Utilities.info = true;
		Utilities.debug = false;

		try {
			Main.procedureTestCyclon();
		} catch (IOException e) {
			e.printStackTrace();
		}

//		AlgorithmCyclonBasic algo = new AlgorithmCyclonBasic();
//		Graph g = new Graph(SimpleNode.class, SimpleEdge.class);
//		g.importFromCSV("graphs/testgraph3.csv");
//		final int SAMPLE_SIZE = 1000;
//		final int SHUFFLE_INTERVAL = 1;
//		final int CACHE_SIZE = 6;
//		final int SHUFFLE_LENGTH = 4;
//		final double CONFIDENCE_LEVEL = 0.90;
//		Test test = new Test(algo, g);
//		test.runFullTest(0, SAMPLE_SIZE, SHUFFLE_INTERVAL, CACHE_SIZE, SHUFFLE_LENGTH, CONFIDENCE_LEVEL);
//		Utilities.printInfo((int) (100.0*algo.nbEchanges/(algo.nbEchanges+algo.nbEchecs)) + "% de shuffling");
//		g.exportToCSV("graphs/output.csv");

//		Graph g = new Graph(SimpleNode.class,SimpleEdge.class);
//		g.generateRandom(100,10);
//		g.exportToCSV("graphs/testgraph4.csv");
//		System.out.println(g);

//		AlgorithmIncrementReference ref = new AlgorithmIncrementReference();
//		Test test = new Test(ref,g);
//		test.runFullTest(0,SAMPLE_SIZE,SHUFFLE_INTERVAL,CACHE_SIZE,SHUFFLE_LENGTH,CONFIDENCE_LEVEL);

//		AlgorithmRandomReference ref = new AlgorithmRandomReference();
//		Test test = new Test(ref,g);
//		test.runFullTest(0,SAMPLE_SIZE,SHUFFLE_INTERVAL,CACHE_SIZE,SHUFFLE_LENGTH,CONFIDENCE_LEVEL);

	}
}

