package i5.las2peer.services.ocd.algorithms.centrality;

import i5.las2peer.services.ocd.graphs.CentralityCreationType;
import i5.las2peer.services.ocd.utils.SimpleFactory;

public class CentralityAlgorithmFactory implements SimpleFactory<CentralityAlgorithm, CentralityCreationType> {

	public boolean isInstantiatable(CentralityCreationType creationType) {
		if(creationType.correspondsAlgorithm()) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public CentralityAlgorithm getInstance(CentralityCreationType creationType) throws InstantiationException, IllegalAccessException {
		if(isInstantiatable(creationType)) {
			CentralityAlgorithm algorithm = (CentralityAlgorithm) creationType.getCreationMethodClass().newInstance();
			return algorithm;
		}
		throw new IllegalStateException("This creation type is not an instantiatable algorithm.");
	}

}
