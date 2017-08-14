package i5.las2peer.services.ocd.algorithms.centrality;

import java.util.HashSet;
import java.util.Set;

import i5.las2peer.services.ocd.graphs.CentralityCreationLog;
import i5.las2peer.services.ocd.graphs.CentralityCreationType;
import i5.las2peer.services.ocd.graphs.CentralityMap;
import i5.las2peer.services.ocd.graphs.CustomGraph;
import i5.las2peer.services.ocd.graphs.GraphType;

public class Radiality implements CentralityAlgorithm {
	
	public CentralityMap getValues(CustomGraph graph) {
		Integration integrationAlgorithm = new Integration();
		CentralityMap res = integrationAlgorithm.getValues(graph);
		
		res.setCreationMethod(new CentralityCreationLog(CentralityCreationType.RADIALITY, this.compatibleGraphTypes()));	
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
		return CentralityCreationType.RADIALITY;
	}
}
