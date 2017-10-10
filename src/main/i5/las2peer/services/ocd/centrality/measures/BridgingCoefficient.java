package i5.las2peer.services.ocd.centrality.measures;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.la4j.matrix.Matrix;

import i5.las2peer.services.ocd.graphs.CentralityCreationLog;
import i5.las2peer.services.ocd.graphs.CentralityCreationType;
import i5.las2peer.services.ocd.graphs.CentralityMap;
import i5.las2peer.services.ocd.graphs.CustomGraph;
import i5.las2peer.services.ocd.graphs.GraphType;
import y.base.Node;
import y.base.NodeCursor;

public class BridgingCoefficient implements CentralityAlgorithm {
	
	public CentralityMap getValues(CustomGraph graph) throws InterruptedException {
		NodeCursor nc = graph.nodes();
		CentralityMap res = new CentralityMap(graph);
		res.setCreationMethod(new CentralityCreationLog(CentralityCreationType.BRIDGING_COEFFICIENT, this.getParameters(), this.compatibleGraphTypes()));
		
		Matrix A = graph.getNeighbourhoodMatrix();
		int n = graph.nodeCount();
		
		while(nc.ok()) {
			if(Thread.interrupted()) {
				throw new InterruptedException();
			}
			Node node = nc.node();	
			int v = node.index();
			
			// Calculate the probability of leaving the direct neighborhood subgraph in two steps
			double leavingProbability = 0.0;
			double nodeWeightedOutDegree = 0.0;
			
			for(int i = 0; i < n; i++) {
				double nodeEdgeWeight = A.get(v, i);
				if(nodeEdgeWeight > 0) {
					nodeWeightedOutDegree += nodeEdgeWeight;
					double neighborWeightedOutDegree = 0.0;
					double neighborLeavingProbability = 0.0;
					for(int j = 0; j < n; j++) {
						double neighborEdgeWeight = A.get(i, j);
						if(neighborEdgeWeight > 0 && j != v) {
							neighborWeightedOutDegree += neighborEdgeWeight;
							if(A.get(v, j) == 0) {
								neighborLeavingProbability += nodeEdgeWeight * neighborEdgeWeight;
							}
						}
					}
					if(neighborWeightedOutDegree != 0)
						neighborLeavingProbability /= neighborWeightedOutDegree;
					
					leavingProbability += neighborLeavingProbability;
				}
			}
			
			if(nodeWeightedOutDegree != 0)
				leavingProbability /= nodeWeightedOutDegree;
			
			res.setNodeValue(node, leavingProbability);
			nc.next();
		}
		return res;
	}

	@Override
	public Set<GraphType> compatibleGraphTypes() {
		Set<GraphType> compatibleTypes = new HashSet<GraphType>();
		compatibleTypes.add(GraphType.DIRECTED);
		compatibleTypes.add(GraphType.WEIGHTED);
		return compatibleTypes;
	}

	@Override
	public CentralityCreationType getAlgorithmType() {
		return CentralityCreationType.BRIDGING_COEFFICIENT;
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
