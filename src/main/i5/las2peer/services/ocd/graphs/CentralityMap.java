package i5.las2peer.services.ocd.graphs;

import java.util.HashMap;
import java.util.Map;

import y.base.Node;

public class CentralityMap {
	private CustomGraph graph;
	private Map<Node, Double> map = new HashMap<Node, Double>();
	private CentralityCreationType type;
	
	public CentralityMap(CustomGraph graph, CentralityCreationType type) {
		this.graph = graph;
		this.setType(type);
	}
	
	public void setNodeValue(Node node, double value) {
		// TODO: Does this work?
		if(graph.contains(node)) {
			map.put(node, value);
		}
	}
	
	public double getNodeValue(CustomNode node) {
		return map.get(node);
	}

	public CentralityCreationType getType() {
		return type;
	}

	public void setType(CentralityCreationType type) {
		this.type = type;
	}
}
