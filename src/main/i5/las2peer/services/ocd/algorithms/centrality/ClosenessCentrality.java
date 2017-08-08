package i5.las2peer.services.ocd.algorithms.centrality;

import java.util.HashSet;
import java.util.Set;

import i5.las2peer.services.ocd.graphs.CentralityCreationLog;
import i5.las2peer.services.ocd.graphs.CentralityCreationType;
import i5.las2peer.services.ocd.graphs.CentralityMap;
import i5.las2peer.services.ocd.graphs.CustomGraph;
import i5.las2peer.services.ocd.graphs.GraphType;
import y.base.Node;
import y.base.NodeCursor;
import y.algo.ShortestPaths;

public class ClosenessCentrality implements CentralityAlgorithm {
	
	public CentralityMap getValues(CustomGraph graph) {
		NodeCursor nc = graph.nodes();
		
		CentralityMap res = new CentralityMap(graph);
		res.setCreationMethod(new CentralityCreationLog(CentralityCreationType.CLOSENESS_CENTRALITY, this.compatibleGraphTypes()));
		
		double[] edgeWeights = graph.getEdgeWeights();
		while(nc.ok()) {
			Node node = nc.node();
			double[] dist = new double[graph.nodeCount()];
			ShortestPaths.dijkstra(graph, node, true, edgeWeights, dist);
			double distSum = 0.0;
			for(double d : dist) {
				distSum += d;
			}
			res.setNodeValue(node, (graph.nodeCount()-1)/distSum);
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
		return CentralityCreationType.CLOSENESS_CENTRALITY;
	}
}
