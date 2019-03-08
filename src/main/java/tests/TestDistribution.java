package tests;

import algorithms.AbstractAlgorithm;
import models.*;
import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.TDistribution;
import utilities.Utilities;
import java.util.*;

public class TestDistribution implements Runnable {

	private ArrayList<Integer> samples;
	private ArrayList<Double> values;

	public TestDistribution(ArrayList<Integer> samples, ArrayList<Double> values) {
		this.samples = samples;
		this.values = values;
	}

	public void run(){
		System.out.println("TestDistribution running");
	}

}
