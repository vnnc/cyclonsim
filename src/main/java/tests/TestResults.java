package tests;

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
		return line;
	}
}


