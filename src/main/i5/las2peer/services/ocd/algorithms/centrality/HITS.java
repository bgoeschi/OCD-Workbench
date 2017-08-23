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
import y.base.NodeCursor;

public class HITS implements CentralityAlgorithm {
	
	public CentralityMap getValues(CustomGraph graph) throws InterruptedException {
		NodeCursor nc = graph.nodes();
		CentralityMap res = new CentralityMap(graph);
		res.setCreationMethod(new CentralityCreationLog(CentralityCreationType.HITS, this.compatibleGraphTypes()));
		
		int n = nc.size();
		Matrix A = graph.getNeighbourhoodMatrix();
		Vector hubWeights = new BasicVector(n);
		Vector authorityWeights = new BasicVector(n);
		
		//Set all weights to 1
		for(int i = 0; i < n; i++) {
			hubWeights.set(i, 1.0);
			authorityWeights.set(i, 1.0);
		}
		
		Vector oldA = new BasicVector(n);
		Vector oldH = new BasicVector(n);
		
		while(!authorityWeights.equals(oldA) || !hubWeights.equals(oldH)) {
			//Copy old values
			for(int i = 0; i < n; i++) {
				oldA.set(i, authorityWeights.get(i));
				oldH.set(i, hubWeights.get(i));
			}
			
			//Update authority weights
			for(int i = 0; i < n; i++) {
				double newValue = 0.0;
				for(int j = 0; j < n; j++) {
					newValue += A.get(j, i) * hubWeights.get(j);
				}
				authorityWeights.set(i, newValue);
			}
			
			//Update hub weights
			for(int i = 0; i < n; i++) {
				double newValue = 0.0;
				for(int j = 0; j < n; j++) {
					newValue += A.get(i, j) * authorityWeights.get(j);
				}
				hubWeights.set(i, newValue);
			}
			
			//Normalize
			double normA = MatrixOperations.norm(authorityWeights);
			authorityWeights = authorityWeights.divide(normA);
			double normH = MatrixOperations.norm(hubWeights);
			hubWeights = hubWeights.divide(normH);
		}
		
		//Set centrality values to the authority weights
		while(nc.ok()) {
			res.setNodeValue(nc.node(), authorityWeights.get(nc.node().index()));
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
		return CentralityCreationType.HITS;
	}
}
