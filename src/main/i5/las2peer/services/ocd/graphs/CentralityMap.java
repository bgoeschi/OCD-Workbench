package i5.las2peer.services.ocd.graphs;

import java.util.HashMap;
import java.util.Map;

import y.base.Node;

public class CentralityMap {
	private CustomGraph graph;
	private Map<Integer, Double> map = new HashMap<Integer, Double>();
	private CentralityCreationType type;
	
	public CentralityMap(CustomGraph graph, CentralityCreationType type) {
		this.graph = graph;
		this.setType(type);
	}
	
	public void setNodeValue(Node node, double value) {
		if(graph.contains(node)) {
			map.put(node.index(), value);
		}
	}
	
	public double getNodeValue(Node node) {
		return map.get(node.index());
	}
	
	public double getNodeIndexValue(int index) {
		return map.get(index);
	}

	public CentralityCreationType getType() {
		return type;
	}

	public void setType(CentralityCreationType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "map=" + map;
	}
}
