package i5.las2peer.services.ocd.algorithms.centrality;

import java.util.HashSet;
import java.util.Set;

import i5.las2peer.services.ocd.graphs.GraphType;
import y.base.Graph;
import y.base.Node;
import y.base.NodeCursor;

public class DegreeCentrality implements CentralityAlgorithm {
	Graph graph;
	
	public DegreeCentrality(Graph graph) {
		this.graph = graph;
	}
	
	@Override
	public double[] getValues() {
		NodeCursor n = graph.nodes();
		double[] res = new double[graph.nodeCount()];
		int i = 0;
		while(n.ok()) {
			Node node = n.node();
			res[i] = node.degree();
			i++;
			n.next();
		}
		return res;
	}
	
	public double[] getNormalizedValues() {
		double [] res = getValues();
		
		for(int i = 0; i < res.length; i++) {
			res[i] /= graph.nodeCount() - 1;
		}
		return res;
	}

	@Override
	public Set<GraphType> compatibleGraphTypes() {
		Set<GraphType> compatibleTypes = new HashSet<GraphType>();
		//TODO: compatibleTypes.add(GraphType.WEIGHTED);
		return compatibleTypes;
	}
}
