package i5.las2peer.services.ocd.algorithms.utils;

import org.la4j.matrix.Matrix;
import org.la4j.vector.Vector;
import org.la4j.vector.dense.BasicVector;

public class MatrixOperations {
	
	public static Vector calculatePrincipalEigenvector(Matrix matrix) throws InterruptedException {
		Vector x = new BasicVector(matrix.columns());
		Vector oldX = new BasicVector(matrix.columns());
		for(int i = 0; i < x.length(); i++) {
			x.set(i, 1.0);
			oldX.set(i, 0.0);
		}

		for(int i = 0; i < 50; i++) { // i is set rather small, so the method does not take that long for bigger matrices
			if(Thread.interrupted()) {
				throw new InterruptedException();
			}
			oldX = x;
			x = matrix.multiply(x);	
			double norm = norm(x);
			x = x.divide(norm);
		}	
		return x;
	}
	
	public static double calculateAbsolutePrincipalEigenvalue(Matrix matrix) throws InterruptedException {
		Vector x = calculatePrincipalEigenvector(matrix);
		return norm(matrix.multiply(x));
	}
	
	/**
	 * Calculates the euclidean norm of the given vector
	 */
	public static double norm(Vector v) {
		double squareSum = 0.0;
		for(int i = 0; i < v.length(); i++) {
			squareSum += v.get(i) * v.get(i);
		}
		return Math.sqrt(squareSum);
	}
}
