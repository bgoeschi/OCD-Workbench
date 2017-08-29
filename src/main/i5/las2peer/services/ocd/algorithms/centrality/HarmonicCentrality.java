package i5.las2peer.services.ocd.algorithms.centrality;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import i5.las2peer.services.ocd.graphs.CentralityCreationLog;
import i5.las2peer.services.ocd.graphs.CentralityCreationType;
import i5.las2peer.services.ocd.graphs.CentralityMap;
import i5.las2peer.services.ocd.graphs.CustomGraph;
import i5.las2peer.services.ocd.graphs.GraphType;
import y.algo.ShortestPaths;
import y.base.Node;
import y.base.NodeCursor;

public class HarmonicCentrality implements CentralityAlgorithm {
	
	public CentralityMap getValues(CustomGraph graph) throws InterruptedException {
		NodeCursor nc = graph.nodes();
		
		CentralityMap res = new CentralityMap(graph);
		res.setCreationMethod(new CentralityCreationLog(CentralityCreationType.HARMONIC_CENTRALITY, this.getParameters(), this.compatibleGraphTypes()));
		
		double[] edgeWeights = graph.getEdgeWeights();
		while(nc.ok()) {
			if(Thread.interrupted()) {
				throw new InterruptedException();
			}
			Node node = nc.node();
			double[] dist = new double[graph.nodeCount()];
			ShortestPaths.dijkstra(graph, node, true, edgeWeights, dist);
			double inverseDistSum = 0.0;
			for(double d : dist) {
				if(d != 0.0) {
					inverseDistSum += 1.0/d;
				}
			}
			res.setNodeValue(node, 1.0/(graph.nodeCount()-1.0)*inverseDistSum);
			nc.next();
		}
		
		return res;
	}

	@Override
	public Set<GraphType> compatibleGraphTypes() {
		Set<GraphType> compatibleTypes = new HashSet<GraphType>();
		compatibleTypes.add(GraphType.WEIGHTED);
		return compatibleTypes;
	}

	@Override
	public CentralityCreationType getAlgorithmType() {
		return CentralityCreationType.HARMONIC_CENTRALITY;
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
