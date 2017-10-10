package i5.las2peer.services.ocd.centrality.measures;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import i5.las2peer.services.ocd.graphs.CentralityCreationLog;
import i5.las2peer.services.ocd.graphs.CentralityCreationType;
import i5.las2peer.services.ocd.graphs.CentralityMap;
import i5.las2peer.services.ocd.graphs.CustomGraph;
import i5.las2peer.services.ocd.graphs.GraphType;
import y.base.Node;
import y.base.NodeCursor;
import y.algo.ShortestPaths;

public class Eccentricity implements CentralityAlgorithm {
	
	public CentralityMap getValues(CustomGraph graph) throws InterruptedException {
		NodeCursor nc = graph.nodes();
		
		CentralityMap res = new CentralityMap(graph);
		res.setCreationMethod(new CentralityCreationLog(CentralityCreationType.ECCENTRICITY, this.getParameters(), this.compatibleGraphTypes()));
		
		double[] edgeWeights = graph.getEdgeWeights();
		while(nc.ok()) {
			if(Thread.interrupted()) {
				throw new InterruptedException();
			}
			Node node = nc.node();
			double[] dist = new double[graph.nodeCount()];
			ShortestPaths.dijkstra(graph, node, true, edgeWeights, dist);
			double max = 0.0;
			for(double d : dist) {
				if(d > max)
					max = d;
			}
			if(max == 0) {
				res.setNodeValue(node, 0);
			}
			else {
				res.setNodeValue(node, 1/max);
			}
			nc.next();
		}
		return res;
	}
	
	/*public CentralityMap getNormalizedValues(CustomGraph graph) {
		NodeCursor nc = graph.nodes();
		
		CentralityMap res = new CentralityMap(graph);
		res.setCreationMethod(new CentralityCreationLog(CentralityCreationType.ECCENTRICITY, this.compatibleGraphTypes()));
		
		double[] edgeWeights = graph.getEdgeWeights();
		double[] ecvalues = new double[graph.nodeCount()];
		double ecmin = Double.MAX_VALUE;
		double ecmax = 0.0;
		int i = 0;
		while(nc.ok()) {
			if(Thread.interrupted()) {
				throw new InterruptedException();
			}
			Node node = nc.node();
			double[] dist = new double[graph.nodeCount()];
			ShortestPaths.dijkstra(graph, node, true, edgeWeights, dist);
			double max = 0.0;
			for(double d : dist) {
				if(d > max)
					max = d;
			}
			// Find the biggest and smallest eccentricity values in the network
			if(max < ecmin)
				ecmin = max;
			if(max > ecmax)
				ecmax = max;
			
			// Save non-normalized values for later
			ecvalues[i] = max;
			
			nc.next();
			i++;
		}
		
		// Normalize values
		nc.toFirst();
		for(double e : ecvalues) {
			Node node = nc.node();
			double normalized = (e - ecmin) / (ecmax - ecmin);
			res.setNodeValue(node, normalized);
			nc.next();
		}
		
		return res;
	}*/

	@Override
	public Set<GraphType> compatibleGraphTypes() {
		Set<GraphType> compatibleTypes = new HashSet<GraphType>();
		compatibleTypes.add(GraphType.WEIGHTED);
		return compatibleTypes;
	}

	@Override
	public CentralityCreationType getAlgorithmType() {
		return CentralityCreationType.ECCENTRICITY;
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
