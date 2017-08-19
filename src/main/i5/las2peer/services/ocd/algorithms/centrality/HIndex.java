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

public class HIndex implements CentralityAlgorithm {
	
	public CentralityMap getValues(CustomGraph graph) throws InterruptedException {
		NodeCursor nc = graph.nodes();
		CentralityMap res = new CentralityMap(graph);
		res.setCreationMethod(new CentralityCreationLog(CentralityCreationType.H_INDEX, this.compatibleGraphTypes()));
		
		while(nc.ok()) {
			if(Thread.interrupted()) {
				throw new InterruptedException();
			}
			Node node = nc.node();
			
			int h = 0;
			boolean checkNext = true;
			NodeCursor neighbors = node.successors();
			
			while(graph.getWeightedNodeDegree(node)/2 >= h && checkNext) {
				checkNext = false;
				neighbors.toFirst();
				int counter = 0;
				
				while(neighbors.ok() && counter < h) {
					if(graph.getWeightedNodeDegree(neighbors.node())/2 >= h) {
						counter++;
					}
					neighbors.next();
				}
				if(counter >= h) {
					h++;
					checkNext = true;
				}
			}
			res.setNodeValue(node, h-1);
			nc.next();
		}
		return res;
	}

	@Override
	public Set<GraphType> compatibleGraphTypes() {
		Set<GraphType> compatibleTypes = new HashSet<GraphType>();
		compatibleTypes.add(GraphType.WEIGHTED);
		return compatibleTypes;
	}

	@Override
	public CentralityCreationType getAlgorithmType() {
		return CentralityCreationType.H_INDEX;
	}
}
