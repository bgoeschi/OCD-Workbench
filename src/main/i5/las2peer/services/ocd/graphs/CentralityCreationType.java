package i5.las2peer.services.ocd.graphs;

/**
 * Analogous to CoverCreationType but for centrality algorithms.
 * @author Tobias
 *
 */
public enum CentralityCreationType {
	
	UNDEFINED(CentralityCreationMethod.class, 0),
	DEGREE_CENTRALITY(i5.las2peer.services.ocd.algorithms.centrality.DegreeCentrality.class, 1);
	
	private final Class<? extends CentralityCreationMethod> creationMethodClass;
	
	/**
	 * For persistence and other purposes.
	 */
	private final int id;
	
	/**
	 * Creates a new instance.
	 * @param creationMethodClass Defines the creationMethodClass attribute.
	 * @param id Defines the id attribute.
	 */
	private CentralityCreationType(Class<? extends CentralityCreationMethod> creationMethodClass, int id) {
		this.creationMethodClass = creationMethodClass;
		this.id = id;
	}
	
	/**
	 * Returns the CoverCreationMethod subclass corresponding to the type.
	 * @return The corresponding class.
	 */
	public Class<? extends CentralityCreationMethod> getCreationMethodClass() {
		return this.creationMethodClass;
	}
}
