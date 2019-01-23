import models.*;
import algorithms.*;

import java.util.Iterator;
import java.util.Set;

public class Main {

	public static void main(String args[]) {
		System.out.println("Running");
		
		AbstractAlgorithm alg = new AlgorithmTest();
		alg.initGraph();
		alg.shuffle();
		
	}
}

