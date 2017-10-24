package i5.las2peer.services.ocd.centrality.measures;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import i5.las2peer.services.ocd.centrality.data.CentralityCreationLog;
import i5.las2peer.services.ocd.centrality.data.CentralityCreationType;
import i5.las2peer.services.ocd.centrality.data.CentralityMeasureType;
import i5.las2peer.services.ocd.centrality.utils.CentralityAlgorithm;
import i5.las2peer.services.ocd.centrality.data.CentralityMap;
import i5.las2peer.services.ocd.graphs.CustomGraph;
import i5.las2peer.services.ocd.graphs.GraphType;
import y.base.Node;
import y.base.NodeCursor;

public class Coreness implements CentralityAlgorithm {
	
	public CentralityMap getValues(CustomGraph graph) throws InterruptedException {
		CentralityMap res = new CentralityMap(graph);
		res.setCreationMethod(new CentralityCreationLog(CentralityMeasureType.CORENESS, CentralityCreationType.CENTRALITY_MEASURE, this.getParameters(), this.compatibleGraphTypes()));
		
		// Save indices of all nodes
		Map<Node, Integer> indices = new HashMap<Node, Integer>();
		NodeCursor nc = graph.nodes();
		
		while(nc.ok()) {
			indices.put(nc.node(), nc.node().index());
			nc.next();
		}
		
		// Execute k-core decomposition
		int k = 0;
		
		while(!graph.isEmpty()) {
			if(Thread.interrupted()) {
				throw new InterruptedException();
			}
			boolean nodeRemoved = true;
			
			while(nodeRemoved == true) {
				nodeRemoved = false;
				nc = graph.nodes();
				
				while(nc.ok()) {
					Node node = nc.node();
					
					if(node.degree()/2 <= k) { // divide by two because in undirected graphs one edge is counted as two edges
						res.setNodeValue(node, k);
						graph.removeNode(node);
						nodeRemoved = true;
					}
					nc.next();
				}
			}
			k++;
		}
		return res;
	}

	@Override
	public Set<GraphType> compatibleGraphTypes() {
		Set<GraphType> compatibleTypes = new HashSet<GraphType>();
		return compatibleTypes;
	}

	@Override
	public CentralityMeasureType getCentralityMeasureType() {
		return CentralityMeasureType.CORENESS;
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
