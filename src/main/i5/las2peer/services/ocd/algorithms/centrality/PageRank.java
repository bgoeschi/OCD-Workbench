package i5.las2peer.services.ocd.algorithms.centrality;

import java.util.HashSet;
import java.util.Set;

import i5.las2peer.services.ocd.graphs.CentralityCreationLog;
import i5.las2peer.services.ocd.graphs.CentralityCreationType;
import i5.las2peer.services.ocd.graphs.CentralityMap;
import i5.las2peer.services.ocd.graphs.CustomGraph;
import i5.las2peer.services.ocd.graphs.GraphType;
import y.base.Edge;
import y.base.EdgeCursor;
import y.base.Node;
import y.base.NodeCursor;

public class PageRank implements CentralityAlgorithm {
	public static final double d = 0.85;
	
	public CentralityMap getValues(CustomGraph graph) throws InterruptedException {
		NodeCursor nc = graph.nodes();
		CentralityMap res = new CentralityMap(graph);
		res.setCreationMethod(new CentralityCreationLog(CentralityCreationType.PAGERANK, this.compatibleGraphTypes()));
		
		//Set initial PageRank of all nodes to 1
		while(nc.ok()) {
			res.setNodeValue(nc.node(), 1.0);
			nc.next();
		}
		nc.toFirst();
		
		int n = nc.size();
		
		for(int k = 0; k < 50; k++) {
			while(nc.ok()) {
				Node i = nc.node();
				double weightedRankSum = 0.0;
				
				EdgeCursor inLinks = i.inEdges();
				while(inLinks.ok()) {
					Edge eji = inLinks.edge();
					Node j = eji.source();

					weightedRankSum += graph.getEdgeWeight(eji) * res.getNodeValue(j) / graph.getWeightedOutDegree(j);
					
					inLinks.next();
				}
				
				double newValue = d * weightedRankSum + (1-d) * 1/n;
				res.setNodeValue(i, newValue);
				
				nc.next();
			}
			nc.toFirst();
		}
		
		//Scale the values, so they sum to 1
		double sum = 0.0;
		while(nc.ok()) {
			sum += res.getNodeValue(nc.node());
			nc.next();
		}
		nc.toFirst();
		double factor = 1/sum;
		
		while(nc.ok()) {
			res.setNodeValue(nc.node(), factor * res.getNodeValue(nc.node()));
			nc.next();
		}
		
		return res;
	}

	@Override
	public Set<GraphType> compatibleGraphTypes() {
		Set<GraphType> compatibleTypes = new HashSet<GraphType>();
		compatibleTypes.add(GraphType.DIRECTED);
		compatibleTypes.add(GraphType.WEIGHTED);
		return compatibleTypes;
	}

	@Override
	public CentralityCreationType getAlgorithmType() {
		return CentralityCreationType.PAGERANK;
	}
}
