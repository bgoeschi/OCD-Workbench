package i5.las2peer.services.ocd.centrality.simulations;

import java.security.InvalidParameterException;
import java.util.Locale;

public enum SimulationType {
	SIR(SirSimulation.class, 0);
	
	/**
	 * The class corresponding to the type
	 */
	private final Class<? extends GraphSimulation> simulationClass;
	
	/**
	 * For persistence and other purposes.
	 */
	private final int id;
	
	/**
	 * Creates a new instance.
	 * @param creationMethodClass Defines the creationMethodClass attribute.
	 * @param id Defines the id attribute.
	 */
	private SimulationType(Class<? extends GraphSimulation> simulationClass, int id) {
		this.simulationClass = simulationClass;
		this.id = id;
	}
	
	/**
	 * Returns the graph simulation subclass corresponding to the type.
	 * @return The corresponding class.
	 */
	public Class<? extends GraphSimulation> getSimulationClass() {
		return this.simulationClass;
	}
	
	/**
	 * Returns the unique id of the type.
	 * @return The id.
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Returns the type corresponding to an id.
	 * @param id The id.
	 * @return The corresponding type.
	 */
	public static SimulationType lookupType(int id) {
        for (SimulationType type : SimulationType.values()) {
            if (id == type.getId()) {
                return type;
            }
        }
        throw new InvalidParameterException();
	}
	
	/**
	 * States whether the corresponding creation method class is actually a CentralityAlgorithm.
	 * @return TRUE if the class is a simulation, otherwise FALSE.
	 */
	public boolean correspondsAlgorithm() {
		if(GraphSimulation.class.isAssignableFrom(this.getSimulationClass())) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Returns the name of the type written in lower case letters and with any underscores replaced by space characters.
	 */
	@Override
	public String toString() {
		String name = name();
		name = name.replace('_', ' ');
		name = name.toLowerCase(Locale.ROOT);
		return name;
	}
}
