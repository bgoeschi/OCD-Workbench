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

public class NeighborhoodCoreness implements CentralityAlgorithm {
	
	public CentralityMap getValues(CustomGraph graph) throws InterruptedException {
		NodeCursor nc = graph.nodes();
		CentralityMap res = new CentralityMap(graph);
		res.setCreationMethod(new CentralityCreationLog(CentralityCreationType.NEIGHBORHOOD_CORENESS, this.getParameters(), this.compatibleGraphTypes()));
		
		//Graph is copied because the coreness algorithm removes all the nodes
		CustomGraph graphCopy = new CustomGraph(graph);
		Coreness corenessAlgorithm = new Coreness();
		CentralityMap corenessMap = corenessAlgorithm.getValues(graphCopy);
		Map<String, Double> nameCorenessMap = corenessMap.getMap();
		
		while(nc.ok()) {
			if(Thread.interrupted()) {
				throw new InterruptedException();
			}
			Node node = nc.node();	
			double neighborCorenessSum = 0.0;
			NodeCursor neighbors = node.successors();
			while(neighbors.ok()) {
				String nodeName = graph.getNodeName(neighbors.node());
				neighborCorenessSum += nameCorenessMap.get(nodeName);
				neighbors.next();
			}
			
			res.setNodeValue(node, neighborCorenessSum);
			nc.next();
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
		return CentralityCreationType.NEIGHBORHOOD_CORENESS;
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
