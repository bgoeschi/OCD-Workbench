package i5.las2peer.services.ocd.algorithms.centrality;

import java.util.HashSet;
import java.util.Set;

import org.la4j.matrix.Matrix;
import org.la4j.matrix.sparse.CCSMatrix;

import i5.las2peer.services.ocd.graphs.CentralityCreationLog;
import i5.las2peer.services.ocd.graphs.CentralityCreationType;
import i5.las2peer.services.ocd.graphs.CentralityMap;
import i5.las2peer.services.ocd.graphs.CustomGraph;
import i5.las2peer.services.ocd.graphs.GraphType;
import y.base.Node;
import y.base.NodeCursor;

public class SubgraphCentrality implements CentralityAlgorithm {
	
	public CentralityMap getValues(CustomGraph graph) throws InterruptedException {
		NodeCursor nc = graph.nodes();
		CentralityMap res = new CentralityMap(graph);
		res.setCreationMethod(new CentralityCreationLog(CentralityCreationType.SUBGRAPH_CENTRALITY, this.compatibleGraphTypes()));
		
		int n = graph.nodeCount();
		Matrix A = graph.getNeighbourhoodMatrix();
		
		while(nc.ok()) {
			res.setNodeValue(nc.node(), 0);
			nc.next();
		}
		
		//TODO: Find suitable maximum value for p
		for(int p = 1; p < 20; p++) {
			if(Thread.interrupted()) {
				throw new InterruptedException();
			}
			Matrix powerOfA = A.power(p);
			long weight = factorial(p);
			nc.toFirst();
			while(nc.ok()) {
				Node node = nc.node();	
				double weightedCycles = powerOfA.get(node.index(), node.index())/weight;
				res.setNodeValue(node, res.getNodeValue(node) + weightedCycles);
				nc.next();
			}
		}
		return res;
	}
	
	private long factorial(long i) {
		if(i <= 1)
			return 1;
		else
			return i * factorial(i-1);
	}

	@Override
	public Set<GraphType> compatibleGraphTypes() {
		Set<GraphType> compatibleTypes = new HashSet<GraphType>();
		compatibleTypes.add(GraphType.DIRECTED);
		return compatibleTypes;
	}

	@Override
	public CentralityCreationType getAlgorithmType() {
		return CentralityCreationType.SUBGRAPH_CENTRALITY;
	}
}
