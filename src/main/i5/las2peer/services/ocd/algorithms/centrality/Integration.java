package i5.las2peer.services.ocd.algorithms.centrality;

import java.util.HashSet;
import java.util.Set;

import i5.las2peer.services.ocd.graphs.CentralityCreationLog;
import i5.las2peer.services.ocd.graphs.CentralityCreationType;
import i5.las2peer.services.ocd.graphs.CentralityMap;
import i5.las2peer.services.ocd.graphs.CustomGraph;
import i5.las2peer.services.ocd.graphs.GraphType;
import y.algo.ShortestPaths;
import y.base.Node;
import y.base.NodeCursor;

public class Integration implements CentralityAlgorithm {
	
	public CentralityMap getValues(CustomGraph graph) {
		NodeCursor nc = graph.nodes();
		CentralityMap res = new CentralityMap(graph);
		res.setCreationMethod(new CentralityCreationLog(CentralityCreationType.INTEGRATION, this.compatibleGraphTypes()));
		
		//Calculate the sum of distances for all nodes and find the diameter of the graph
		double[] edgeWeights = graph.getEdgeWeights();
		double maxDistance = 0;
		while(nc.ok()) {
			Node node = nc.node();
			double[] dist = new double[graph.nodeCount()];
			ShortestPaths.dijkstra(graph, node, true, edgeWeights, dist);
			
			double distSum = 0.0;
			for(double d : dist) {
				distSum += d;
				if(d > maxDistance)
					maxDistance = d;
			}
			res.setNodeValue(node, distSum);
			nc.next();
		}
		
		//Reverse distances and 
		nc.toFirst();
		while(nc.ok()) {
			Node node = nc.node();
			double distSum = res.getNodeValue(node);
			/**
			 * Each distance in the sum is reversed which is equivalent to multiplying n-1 with the 
			 * diameter of the graph and subtracting the sum of distances (+ 1 added to differentiate disconnected nodes).
			 */
			res.setNodeValue(node, ((graph.nodeCount()-1) * (1 + maxDistance) - distSum)/(graph.nodeCount()-1));
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
		return CentralityCreationType.INTEGRATION;
	}
}
