package i5.las2peer.services.ocd.simulation;

import java.util.Map;

import i5.las2peer.services.ocd.utils.ConditionalParameterizableFactory;

public class SimulationFactory implements ConditionalParameterizableFactory<GraphSimulation, SimulationType> {

	@Override
	public boolean isInstantiatable(SimulationType simulationType) {
		if(simulationType.correspondsAlgorithm()) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public GraphSimulation getInstance(SimulationType simulationType, Map<String, String> parameters)
			throws InstantiationException, IllegalAccessException {
		if(isInstantiatable(simulationType)) {
			GraphSimulation simulation = simulationType.getSimulationClass().newInstance();
			simulation.setParameters(parameters);
			return simulation;
		}
		throw new IllegalStateException("This simulation type is not an instantiatable simulation.");
	}

}
