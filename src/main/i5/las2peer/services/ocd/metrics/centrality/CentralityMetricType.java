package i5.las2peer.services.ocd.metrics.centrality;

import java.security.InvalidParameterException;
import java.util.Locale;

/**
 * CentralityMetric registry.
 * Used for factory instantiation, persistence or other context.
 */
public enum CentralityMetricType {

	/*
	 * Each enum constant is instantiated with a corresponding CentralityMetric class object and a UNIQUE id.
	 * Once the framework is in use ids must not be changed to avoid corrupting the persisted data.
	 */
	/**
	 * Abstract type usable e.g. for metrics calculated externally.
	 * Cannot be used for metric instantiation.
	 */
	UNDEFINED (CentralityMetric.class, 0),
	/**
	 * Abstract type for the algorithm execution time.
	 * Cannot be used for metric instantiation.
	 * An execution time metric entry is automatically added to any CentralityMap calculated by a framework algorithm.
	 */
	EXECUTION_TIME (CentralityMetric.class, 1);
	
	/**
	 * For persistence and other purposes.
	 */
	private final int id;
	
	/**
	 * The class corresponding to the type.
	 * Abstract types correspond to the CentralityMetric interface itself.
	 */
	private final Class<? extends CentralityMetric> metricClass;
	
	/**
	 * Creates a new instance.
	 * @param metricClass Defines the metricClass attribute.
	 * @param id Defines the id attribute.
	 */
	private CentralityMetricType(Class<? extends CentralityMetric> metricClass, int id) {
		this.metricClass = metricClass;
		this.id = id;
	}
	
	/**
	 * Returns the CentralityMetric subclass corresponding to the type.
	 * @return The corresponding class.
	 */
	protected Class<? extends CentralityMetric> getMetricClass() {
		return this.metricClass;
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
	public static CentralityMetricType lookupType(int id) {
        for (CentralityMetricType type : CentralityMetricType.values()) {
            if (id == type.getId()) {
                return type;
            }
        }
        throw new InvalidParameterException();
	}
	
	/**
	 * Returns the type corresponding to an CentralityMetric class.
	 * @param metricClass The class.
	 * @return The corresponding type.
	 */
	public static CentralityMetricType lookupType(Class<? extends CentralityMetric> metricClass) {
        for (CentralityMetricType type : CentralityMetricType.values()) {
            if (metricClass == type.getMetricClass()) {
                return type;
            }
        }
        throw new InvalidParameterException();
	}
	
	/**
	 * States whether the corresponding OcdMetric class is a statistical measure.
	 * @return TRUE if the class is a statistical measure, otherwise FALSE.
	 */
	/*public boolean correspondsStatisticalMeasure() {
		if(StatisticalMeasure.class.isAssignableFrom(this.getMetricClass())) {
			return true;
		}
		else {
			return false;
		}
	}*/
	
	/**
	 * States whether the corresponding OcdMetric class is a knowledge-driven measure.
	 * @return TRUE if the class is a knowledge-driven measure, otherwise FALSE.
	 */
	/*public boolean correspondsKnowledgeDrivenMeasure() {
		if(KnowledgeDrivenMeasure.class.isAssignableFrom(this.getMetricClass())) {
			return true;
		}
		else {
			return false;
		}
	}*/
	
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
