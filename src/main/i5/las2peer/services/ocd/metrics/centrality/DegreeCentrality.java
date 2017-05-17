package i5.las2peer.services.ocd.metrics.centrality;

import y.base.Graph;
import y.base.Node;
import y.base.NodeCursor;

public class DegreeCentrality implements CentralityMetric {
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
}
