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
			res.setNodeValue(node, (double) node.degree());
			nc.next();
		}
		return res;
	}
	
	public CentralityMap getNormalizedValues(CustomGraph graph) {
		NodeCursor nc = graph.nodes();
		CentralityMap res = new CentralityMap(graph);
		
		while(nc.ok()) {
			Node node = nc.node();
			res.setNodeValue(node, (double) node.degree()/(graph.nodeCount()-1));
			nc.next();
		}
		return res;
	}

	@Override
	public Set<GraphType> compatibleGraphTypes() {
		Set<GraphType> compatibleTypes = new HashSet<GraphType>();
		//TODO: compatibleTypes.add(GraphType.WEIGHTED);
		return compatibleTypes;
	}

	@Override
	public CentralityCreationType getAlgorithmType() {
		return CentralityCreationType.DEGREE_CENTRALITY;
	}
}
