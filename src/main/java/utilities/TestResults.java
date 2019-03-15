package utilities;

public class TestResults {

	private double meanDistrib;
	private double expectedDistrib;
	private double meanIndep;
	private double expectedIndep;

	public TestResults(double meanDistrib, double expectedDistrib,
	                   double meanIndep, double expectedIndep) {
		this.meanDistrib = meanDistrib;
		this.expectedDistrib = expectedDistrib;
		this.meanIndep = meanIndep;
		this.expectedIndep = expectedIndep;
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
		return this.chiMeanDistrib < this.criticalValueDistrib;
	}
	
	public boolean getIndependencySucceeded () {
		return this.chiMeanIndep < this.criticalValueIndep;
	}
}


