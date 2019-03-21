package tests;

import java.util.*;

public class TestResults {

	private double meanDistrib;
	private double expectedDistrib;
	private double meanIndep;
	private double expectedIndep;
	private ArrayList<Double> distValues = new ArrayList<Double>();
	private ArrayList<Double> indValues = new ArrayList<Double>();

	public TestResults(double meanDistrib, double expectedDistrib,
	                   double meanIndep, double expectedIndep,
	                   ArrayList<Double> independence, ArrayList<Double> distribution) {
		this.meanDistrib = meanDistrib;
		this.expectedDistrib = expectedDistrib;
		this.meanIndep = meanIndep;
		this.expectedIndep = expectedIndep;
		this.indValues = independence;
		this.distValues = distribution;
	}

	public ArrayList<Double> getIndepValues() {
		return indValues;
	}

	public ArrayList<Double> getDistribValues() {
		return distValues;
	}

	public double getExpectedDistrib() {
		return expectedDistrib;
	}

	public double getExpectedIndep() {
		return expectedIndep;
	}

	public boolean getDistributionSucceeded () {
		return this.meanDistrib < this.expectedDistrib;
	}

	public boolean getIndependencySucceeded () {
		return this.meanIndep < this.expectedIndep;
	}

	public String getString () {
		String line = "" + this.getExpectedDistrib();
		line = line + "," + this.getDistributionSucceeded();
		line = line + "," + this.getExpectedIndep();
		line = line + "," + this.getIndependencySucceeded();
		return line;
	}

}


