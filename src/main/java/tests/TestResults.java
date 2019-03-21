package tests;

import java.util.*;

public class TestResults {

	private double meanDistrib;
	private double expectedDistrib;
	private double meanIndep;
	private double expectedIndep;
	private double distribution;
	private double independence;
	private ArrayList<Double> distValues = new ArrayList<Double>();
	private ArrayList<Double> indValues = new ArrayList<Double>();

	public TestResults(double meanDistrib, double expectedDistrib,
	                   double meanIndep, double expectedIndep, ArrayList<Double> independence,ArrayList<Double> distribution) {
		this.meanDistrib = meanDistrib;
		this.expectedDistrib = expectedDistrib;
		this.meanIndep = meanIndep;
		this.expectedIndep = expectedIndep;
		this.indValues = independence;
		this.distValues =distribution;
	}

	public TestResults(double independence, double distribution
					   ) {
		System.out.println("Testresult is called");
		this.independence = independence;
		this.distribution = distribution;

	}



	public double getIndependence() {
		return independence;
	}

	public double getDistribution() {
		return distribution;
	}



	public ArrayList<Double> getInd() {
		return indValues;
	}

	public ArrayList<Double> getDist() {
		return distValues;
	}


	public double getMeanDistrib() {
		return meanDistrib;
	}

	public double getExpectedDistrib() {
		return expectedDistrib;
	}




	public double getMeanIndep() {
		return meanIndep;
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
		String line = this.getMeanDistrib() + "," + this.getExpectedDistrib();
		line = line + "," + this.getDistributionSucceeded();
		line = line + "," + this.getMeanIndep() + "," + this.getExpectedIndep();
		line = line + "," + this.getIndependencySucceeded();
		line = line + "," + this.getInd() + "," + this.getDist();
		return line;
	}

	public String getStringValues () {

		String line = this.getDistribution() + "," + this.getIndependence();
		return line;
	}
}


