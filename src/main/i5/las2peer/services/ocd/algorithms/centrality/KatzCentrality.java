package i5.las2peer.services.ocd.algorithms.centrality;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.la4j.matrix.Matrix;

//import i5.las2peer.services.ocd.algorithms.utils.MatrixOperations;
import i5.las2peer.services.ocd.graphs.CentralityCreationLog;
import i5.las2peer.services.ocd.graphs.CentralityCreationType;
import i5.las2peer.services.ocd.graphs.CentralityMap;
import i5.las2peer.services.ocd.graphs.CustomGraph;
import i5.las2peer.services.ocd.graphs.GraphType;
import y.base.Node;
import y.base.NodeCursor;

public class KatzCentrality implements CentralityAlgorithm {
	private static final double EPSILON = 0.0000000000000000000000000000000000000000000000001;
	
	public CentralityMap getValues(CustomGraph graph) throws InterruptedException {
		NodeCursor nc = graph.nodes();
		CentralityMap res = new CentralityMap(graph);
		res.setCreationMethod(new CentralityCreationLog(CentralityCreationType.KATZ_CENTRALITY, this.getParameters(), this.compatibleGraphTypes()));
		
		int n = graph.nodeCount();
		Matrix A = graph.getNeighbourhoodMatrix();
		
		//TODO: Better to set a fixed value for a or calculate it based on principal eigenvalue?
		
		//double eig = MatrixOperations.calculateAbsolutePrincipalEigenvalue(A);
		//double a = 1/Math.ceil(eig);
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
	public HashMap<String, String> getParameters() {
		return new HashMap<String, String>();
	}
	
	@Override
	public void setParameters(Map<String, String> parameters) throws IllegalArgumentException {
		if(parameters.size() > 0) {
			throw new IllegalArgumentException();
		}
	}
}
