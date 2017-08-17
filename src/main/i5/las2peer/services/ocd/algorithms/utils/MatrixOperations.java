package i5.las2peer.services.ocd.algorithms.utils;

import org.la4j.matrix.Matrix;
import org.la4j.vector.Vector;
import org.la4j.vector.Vectors;
import org.la4j.vector.dense.BasicVector;

public class MatrixOperations {
	private static final double EPSILON = 0.0001;
	
	public static double calculateAbsolutePrincipalEigenvalue(Matrix matrix) {
		Vector x = new BasicVector(matrix.columns());
		Vector oldX = new BasicVector(matrix.columns());
		for(int i = 0; i < x.length(); i++) {
			x.set(i, 1.0);
			oldX.set(i, 0.0);
		}
		
		double norm = 0.0;
		double error = 1.0;

		while(Math.abs(error) > EPSILON) {
		//for(int i = 0; i < 1000; i++) {
			oldX = x;
			x = matrix.multiply(x);	
			norm = x.fold(Vectors.mkEuclideanNormAccumulator());
			//
			Vector errorMatrix = x.subtract(oldX.multiply(norm));
			error = errorMatrix.fold(Vectors.mkEuclideanNormAccumulator());
			//
			x = x.divide(norm);
			
			
		}
		
		return norm;
	}
}
