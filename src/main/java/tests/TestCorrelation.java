package tests;

import algorithms.AbstractAlgorithm;
import models.*;
import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.TDistribution;
import utilities.Utilities;
import java.util.*;

public class TestCorrelation implements Runnable {

	private ArrayList<Integer> samples;
	private ArrayList<Double> values;

	public TestCorrelation(ArrayList<Integer> samples, ArrayList<Double> values) {
		this.samples = samples;
		this.values = values;
	}

	public void run(){
		ArrayList<Double> X = new ArrayList<Double>();
		ArrayList<Double> Y = new ArrayList<Double>();

		int i = 0;
		while(i<=this.samples.size()-2){
			X.add(new Double(this.samples.get(i)));
			Y.add(new Double(this.samples.get(i+1)));
			i=i+2;
		}

		double Xm = TestsMain.computeMean(X);
		double Ym = TestsMain.computeMean(Y);

		double sum1 = 0;
		double sum2 = 0;
		double sum3 = 0;
		for(int j=0;j<(this.samples.size()/2);j++){
			double Xj = X.get(j);
			double Yj = Y.get(j);
			sum1+= (Xj-Xm)*(Yj-Ym);
			sum2+= Math.pow((Xj-Xm),2);
			sum3+= Math.pow((Yj-Ym),2);
		}

		double coefficient = sum1/(Math.sqrt(sum2)*Math.sqrt(sum3));
		this.values.add(coefficient);
	}

}
