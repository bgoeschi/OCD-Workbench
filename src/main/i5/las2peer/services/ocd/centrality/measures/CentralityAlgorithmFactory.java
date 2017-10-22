package i5.las2peer.services.ocd.centrality.measures;

import java.util.Map;

import i5.las2peer.services.ocd.centrality.data.CentralityCreationType;
import i5.las2peer.services.ocd.utils.ConditionalParameterizableFactory;

public class CentralityAlgorithmFactory implements ConditionalParameterizableFactory<CentralityAlgorithm, CentralityCreationType> {

	public boolean isInstantiatable(CentralityCreationType creationType) {
		if(creationType.correspondsAlgorithm()) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public CentralityAlgorithm getInstance(CentralityCreationType creationType, Map<String, String> parameters) throws InstantiationException, IllegalAccessException {
		if(isInstantiatable(creationType)) {
			CentralityAlgorithm algorithm = (CentralityAlgorithm) creationType.getCreationMethodClass().newInstance();
			algorithm.setParameters(parameters);
			return algorithm;
		}
		throw new IllegalStateException("This creation type is not an instantiatable algorithm.");
	}

}
