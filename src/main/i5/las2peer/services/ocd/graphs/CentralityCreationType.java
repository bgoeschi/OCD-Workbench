package i5.las2peer.services.ocd.graphs;

import java.security.InvalidParameterException;
import java.util.Locale;

import i5.las2peer.services.ocd.algorithms.OcdAlgorithm;
import i5.las2peer.services.ocd.algorithms.centrality.CentralityAlgorithm;
import i5.las2peer.services.ocd.benchmarks.GroundTruthBenchmark;

/**
 * Analogous to CoverCreationType but for centrality algorithms.
 * @author Tobias
 *
 */
public enum CentralityCreationType {
	
	UNDEFINED(CentralityCreationMethod.class, 0),
	
	/**
	 * Type corresponding to degree centrality
	 */
	DEGREE_CENTRALITY(i5.las2peer.services.ocd.algorithms.centrality.DegreeCentrality.class, 1);
	
	/**
	 * The class corresponding to the type
	 */
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
	 * Returns the CentralityCreationMethod subclass corresponding to the type.
	 * @return The corresponding class.
	 */
	public Class<? extends CentralityCreationMethod> getCreationMethodClass() {
		return this.creationMethodClass;
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
	public static CentralityCreationType lookupType(int id) {
        for (CentralityCreationType type : CentralityCreationType.values()) {
            if (id == type.getId()) {
                return type;
            }
        }
        throw new InvalidParameterException();
	}
	
	/**
	 * States whether the corresponding creation method class is actually a CentralityAlgorithm.
	 * @return TRUE if the class is a CentralityAlgorithm, otherwise FALSE.
	 */
	public boolean correspondsAlgorithm() {
		if(CentralityAlgorithm.class.isAssignableFrom(this.getCreationMethodClass())) {
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
