package i5.las2peer.services.ocd.simulation;

import java.util.Set;

import i5.las2peer.services.ocd.graphs.GraphType;
import i5.las2peer.services.ocd.utils.Parameterizable;

public interface GraphSimulation extends Parameterizable {
	/**
	 * Returns a log representing the concrete simulation execution.
	 * @return The log.
	 */
	public SimulationType getSimulationType();
	
	/**
	 * Returns all graph types the simulation is compatible with.
	 * @return The compatible graph types.
	 * An empty set if the simulation is not compatible with any type.
	 */
	public Set<GraphType> compatibleGraphTypes();
}
