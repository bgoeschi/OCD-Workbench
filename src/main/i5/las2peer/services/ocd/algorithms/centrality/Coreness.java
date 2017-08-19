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
import y.base.Node;
import y.base.NodeCursor;

public class Coreness implements CentralityAlgorithm {
	
	public CentralityMap getValues(CustomGraph graph) throws InterruptedException {
		CentralityMap res = new CentralityMap(graph);
		res.setCreationMethod(new CentralityCreationLog(CentralityCreationType.CORENESS, this.compatibleGraphTypes()));
		
		//Save indices of all nodes
		Map<Node, Integer> indices = new HashMap<Node, Integer>();
		NodeCursor nc = graph.nodes();
		
		while(nc.ok()) {
			indices.put(nc.node(), nc.node().index());
			nc.next();
		}
		
		//Execute k-core decomposition
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
					
					if(node.degree()/2 <= k) { //divide by two because in undirected graphs one edge is counted as two edges
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
	public CentralityCreationType getAlgorithmType() {
		return CentralityCreationType.CORENESS;
	}
}
