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

public class Integration implements CentralityAlgorithm {
	
	public CentralityMap getValues(CustomGraph graph) throws InterruptedException {
		NodeCursor nc = graph.nodes();
		CentralityMap res = new CentralityMap(graph);
		res.setCreationMethod(new CentralityCreationLog(CentralityCreationType.INTEGRATION, this.compatibleGraphTypes()));
		
		//Calculate the sum of distances and the number of reachable nodes for all nodes and find the diameter of the graph
		double[] edgeWeights = graph.getEdgeWeights();
		Map<Node, Integer> reachableNodes = new HashMap<Node, Integer>();
		double maxDistance = 0;
		while(nc.ok()) {
			if(Thread.interrupted()) {
				throw new InterruptedException();
			}
			Node node = nc.node();
			double[] dist = new double[graph.nodeCount()];
			
			ShortestPaths.dijkstra(graph, node, true, edgeWeights, dist);
			double distSum = 0.0;
			int reachableNodesCounter = 0;
			for(double d : dist) {
				if(d != Double.POSITIVE_INFINITY && d != 0) {
					distSum += d;
					reachableNodesCounter++;
					if(d > maxDistance)
						maxDistance = d;
				}
			}
			reachableNodes.put(node, reachableNodesCounter);
			res.setNodeValue(node, distSum);
			nc.next();
		}
		
		//Reverse distances
		nc.toFirst();
		while(nc.ok()) {
			Node node = nc.node();
			double distSum = res.getNodeValue(node);
			/**
			 * Each distance in the sum is reversed which is equivalent to multiplying the number of reachable nodes with the 
			 * diameter of the graph and subtracting the sum of distances (+ 1 added to differentiate disconnected nodes).
			 */
			res.setNodeValue(node, (reachableNodes.get(node) * (1 + maxDistance) - distSum)/(graph.nodeCount()-1));
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
