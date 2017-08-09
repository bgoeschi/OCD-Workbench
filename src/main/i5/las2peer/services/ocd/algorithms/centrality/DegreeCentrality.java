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

public class DegreeCentrality implements CentralityAlgorithm {
	
	public CentralityMap getValues(CustomGraph graph) {
		NodeCursor nc = graph.nodes();
		CentralityMap res = new CentralityMap(graph);
		res.setCreationMethod(new CentralityCreationLog(CentralityCreationType.DEGREE_CENTRALITY, this.compatibleGraphTypes()));
		
		while(nc.ok()) {
			Node node = nc.node();
			/**
			 * In an undirected graph each edge corresponds to two edges (a->b and b->a) and
			 * directed graphs are made undirected before the execution.
			 * Since each edge should only be counted once, the degree is divided by 2.
			**/
			res.setNodeValue(node, graph.getWeightedNodeDegree(node)/2);
			nc.next();
		}
		return res;
	}
	
	/*public CentralityMap getNormalizedValues(CustomGraph graph) {
		NodeCursor nc = graph.nodes();
		CentralityMap res = new CentralityMap(graph);
		res.setCreationMethod(new CentralityCreationLog(CentralityCreationType.DEGREE_CENTRALITY, this.compatibleGraphTypes()));
		
		while(nc.ok()) {
			Node node = nc.node();
			res.setNodeValue(node, (double) node.degree()/2*(graph.nodeCount()));
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
		return CentralityCreationType.DEGREE_CENTRALITY;
	}
}
