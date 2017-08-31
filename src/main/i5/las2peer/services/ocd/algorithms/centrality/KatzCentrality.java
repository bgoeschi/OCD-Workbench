package i5.las2peer.services.ocd.algorithms.centrality;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.la4j.inversion.GaussJordanInverter;
import org.la4j.inversion.MatrixInverter;
import org.la4j.matrix.Matrix;
import org.la4j.matrix.sparse.CCSMatrix;
import org.la4j.vector.Vector;
import org.la4j.vector.dense.BasicVector;

import i5.las2peer.services.ocd.graphs.CentralityCreationLog;
import i5.las2peer.services.ocd.graphs.CentralityCreationType;
import i5.las2peer.services.ocd.graphs.CentralityMap;
import i5.las2peer.services.ocd.graphs.CustomGraph;
import i5.las2peer.services.ocd.graphs.GraphType;
import y.base.Node;
import y.base.NodeCursor;

public class KatzCentrality implements CentralityAlgorithm {
	private double alpha = 0.1;
	/*
	 * PARAMETER NAMES
	 */
	protected static final String ALPHA_NAME = "Alpha";
	
	/*private static final double EPSILON = 0.0000000000000000000000000000000000000000000000001;
	
	public CentralityMap getValues(CustomGraph graph) throws InterruptedException {
		NodeCursor nc = graph.nodes();
		CentralityMap res = new CentralityMap(graph);
		res.setCreationMethod(new CentralityCreationLog(CentralityCreationType.KATZ_CENTRALITY, this.getParameters(), this.compatibleGraphTypes()));
		
		int n = graph.nodeCount();
		Matrix A = graph.getNeighbourhoodMatrix();

		double a = 0.2;
		
		while(nc.ok()) {
			if(Thread.interrupted()) {
				throw new InterruptedException();
			}
			Node node = nc.node();	
			
			int i = node.index();
			int k = 1;
			double katzScore = 0.0;
			while(Math.pow(a, k) > EPSILON) {
				for(int j = 0; j < n; j++) {
					katzScore += Math.pow(a, k) * A.power(k).get(j, i);
				}
				k++;
			}
			
			res.setNodeValue(node, katzScore);
			nc.next();
		}
		return res;
	}*/
	
	public CentralityMap getValues(CustomGraph graph) throws InterruptedException {
		NodeCursor nc = graph.nodes();
		CentralityMap res = new CentralityMap(graph);
		res.setCreationMethod(new CentralityCreationLog(CentralityCreationType.KATZ_CENTRALITY, this.getParameters(), this.compatibleGraphTypes()));
		
		int n = graph.nodeCount();
		Matrix A = graph.getNeighbourhoodMatrix();
		
		//Create identity matrix and vector consisting of only ones
		Matrix I = new CCSMatrix(n, n);
		Vector ones = new BasicVector(n);
		for(int i = 0; i < n; i++) {
			I.set(i, i, 1.0);
			ones.set(i, 1.0);
		}
		
		Matrix toInvert = I.subtract(A.multiply(alpha));
		MatrixInverter gauss = new GaussJordanInverter(toInvert);
		Matrix inverse = gauss.inverse();
		
		Vector resultVector = inverse.multiply(ones);
		
		while(nc.ok()) {
			if(Thread.interrupted()) {
				throw new InterruptedException();
			}
			Node node = nc.node();
			res.setNodeValue(node, resultVector.get(node.index()));
			nc.next();
		}
		return res;
	}

	@Override
	public Set<GraphType> compatibleGraphTypes() {
		Set<GraphType> compatibleTypes = new HashSet<GraphType>();
		compatibleTypes.add(GraphType.DIRECTED);
		return compatibleTypes;
	}

	@Override
	public CentralityCreationType getAlgorithmType() {
		return CentralityCreationType.KATZ_CENTRALITY;
	}
	
	@Override
	public Map<String, String> getParameters() {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(ALPHA_NAME, Double.toString(alpha));
		return parameters;
	}
	
	@Override
	public void setParameters(Map<String, String> parameters) throws IllegalArgumentException {
		if(parameters.containsKey(ALPHA_NAME)) {
			alpha = Double.parseDouble(parameters.get(ALPHA_NAME));
			parameters.remove(ALPHA_NAME);
		}
		if(parameters.size() > 0) {
			throw new IllegalArgumentException();
		}
	}
}
