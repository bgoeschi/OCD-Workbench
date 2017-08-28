package i5.las2peer.services.ocd.algorithms.centrality;

import java.util.HashSet;
import java.util.Set;

import org.la4j.matrix.Matrix;
import org.la4j.vector.Vector;
import org.la4j.vector.dense.BasicVector;

import i5.las2peer.services.ocd.algorithms.utils.MatrixOperations;
import i5.las2peer.services.ocd.graphs.CentralityCreationLog;
import i5.las2peer.services.ocd.graphs.CentralityCreationType;
import i5.las2peer.services.ocd.graphs.CentralityMap;
import i5.las2peer.services.ocd.graphs.CustomGraph;
import i5.las2peer.services.ocd.graphs.GraphType;
import y.base.Node;
import y.base.NodeCursor;

public class BargainingCentrality implements CentralityAlgorithm {
	private static final double ALPHA = 1;
	private static final double BETA = 0.5;
	
	public CentralityMap getValues(CustomGraph graph) throws InterruptedException {
		NodeCursor nc = graph.nodes();
		CentralityMap res = new CentralityMap(graph);
		res.setCreationMethod(new CentralityCreationLog(CentralityCreationType.BARGAINING_CENTRALITY, this.compatibleGraphTypes()));
		
		int n = nc.size();
		Matrix R = graph.getNeighbourhoodMatrix();
		Vector c = new BasicVector(n);
		
		for(int k = 0; k < 50; k++) {
			while(nc.ok()) {
				if(Thread.interrupted()) {
					throw new InterruptedException();
				}
				Node i = nc.node();
				double sum = 0.0;
				NodeCursor neighbors = i.successors();
				while(neighbors.ok()) {
					Node j = neighbors.node();
					double Rij = R.get(i.index(), j.index());
					double cj = c.get(j.index());
					sum += (ALPHA + BETA * cj) * Rij;
					neighbors.next();
				}	
				c.set(i.index(), sum);
				nc.next();
			}
			nc.toFirst();
		}
		
		double norm = MatrixOperations.norm(c);
		c = c.multiply(n/norm);
		
		nc.toFirst();
		while(nc.ok()) {
			Node node = nc.node();
			res.setNodeValue(node, c.get(node.index()));
			nc.next();
		}

		return res;
	}

	@Override
	public Set<GraphType> compatibleGraphTypes() {
		Set<GraphType> compatibleTypes = new HashSet<GraphType>();
		compatibleTypes.add(GraphType.DIRECTED);
		return compatibleTypes;
	}

	@Override
	public CentralityCreationType getAlgorithmType() {
		return CentralityCreationType.BARGAINING_CENTRALITY;
	}
}
