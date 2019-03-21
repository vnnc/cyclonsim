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
		g.importFromCSV("graphs/testgraph3.csv");

		final String FILE_NUMBER = "1";
		FileWriter fw = new FileWriter("statistical_data/resultats_shuffle_"+FILE_NUMBER+".csv", false);
		FileWriter fw2 = new FileWriter("statistical_data/annexe_resultats_"+FILE_NUMBER+".csv",false);

		BufferedWriter bw = new BufferedWriter(fw);
		bw.write("ID,SHUFFLE_INTERVAL,KHI2_DISTRIB_THEO,KHI2_DISTRIB_SUCC,KHI2_INDEP_THEO,KHI2_INDEP_SUCC");
		bw.write(System.getProperty("line.separator"));

		BufferedWriter bw2 = new BufferedWriter(fw2);
		bw2.write("SHUFFLE_INTERVAL,DIST_VALUE,INDEP_VALUE");
		bw2.write(System.getProperty("line.separator"));

		final int SAMPLE_SIZE = 50;
		int SHUFFLE_INTERVAL;
		final int CACHE_SIZE = 6;
		final int SHUFFLE_LENGTH = 4;
		final double CONFIDENCE_LEVEL = 0.90;
		
		Tests test = new Tests(algo, g);
		int id = 1;

		final int MAX_INTERVAL = 2;
		
		for(SHUFFLE_INTERVAL=1; SHUFFLE_INTERVAL<=MAX_INTERVAL; SHUFFLE_INTERVAL++) {
			algo.setInterval(SHUFFLE_INTERVAL);
			TestResults res = test.runFullTest(SAMPLE_SIZE, CACHE_SIZE, SHUFFLE_LENGTH, CONFIDENCE_LEVEL);
			String line = id + "," + SHUFFLE_INTERVAL + "," + res.getString();
			bw.write(line);
			bw.write(System.getProperty("line.separator"));
			bw.flush();

			ArrayList<Double> distributionValues = res.getDistribValues();
			ArrayList<Double> independenceValues = res.getIndepValues();

			for(int k=0;k<distributionValues.size();k++){
				line = id+","+distributionValues.get(k)+","+independenceValues.get(k);
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
	
	public static void main(String args[]) {
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
//		g.generateRandom(20,6);
//		g.exportToCSV("graphs/testgraph3.csv");
//		System.out.println(g);

//		AlgorithmIncrementReference ref = new AlgorithmIncrementReference();
//		Test test = new Test(ref,g);
//		test.runFullTest(0,SAMPLE_SIZE,SHUFFLE_INTERVAL,CACHE_SIZE,SHUFFLE_LENGTH,CONFIDENCE_LEVEL);

//		AlgorithmRandomReference ref = new AlgorithmRandomReference();
//		Test test = new Test(ref,g);
//		test.runFullTest(0,SAMPLE_SIZE,SHUFFLE_INTERVAL,CACHE_SIZE,SHUFFLE_LENGTH,CONFIDENCE_LEVEL);

	}
}

