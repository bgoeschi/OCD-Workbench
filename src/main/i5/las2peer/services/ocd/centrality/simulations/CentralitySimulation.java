package i5.las2peer.services.ocd.centrality.simulations;

import java.util.Set;

import i5.las2peer.services.ocd.graphs.GraphType;
import i5.las2peer.services.ocd.utils.Parameterizable;

public interface CentralitySimulation extends Parameterizable {
	/**
	 * Returns a log representing the concrete simulation execution.
	 * @return The log.
	 */
	public CentralitySimulationType getSimulationType();
	
	/**
	 * Returns all graph types the simulation is compatible with.
	 * @return The compatible graph types.
	 * An empty set if the simulation is not compatible with any type.
	 */
	public Set<GraphType> compatibleGraphTypes();
}
