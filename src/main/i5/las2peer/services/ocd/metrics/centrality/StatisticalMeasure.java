package i5.las2peer.services.ocd.metrics.centrality;

import i5.las2peer.services.ocd.algorithms.utils.CentralityAlgorithmException;
import i5.las2peer.services.ocd.graphs.CentralityMap;
import i5.las2peer.services.ocd.graphs.GraphType;

import java.util.Set;

/**
 * A common interface for statistical centrality metrics.
 * @author Tobias
 *
 */
public interface StatisticalMeasure extends CentralityMetric {

	/**
	 * Measures a CentralityMap, adds the resulting metric log to it.
	 * Implementations of this method must allow to be interrupted.
	 * I.e. they must periodically check the thread for interrupts
	 * and throw an InterruptedException if an interrupt was detected.
	 * @param map The CentralityMap which is evaluated. It must also contain the corresponding graph.
	 * @return The calculated metric value.
	 * @throws CentralityMetricException If the execution failed.
	 * @throws InterruptedException If the executing thread was interrupted.
	 * @throws CentralityAlgorithmException 
	 */
	public double measure(CentralityMap map) throws CentralityMetricException, InterruptedException, CentralityAlgorithmException;
	
	/**
	 * Returns the graph types which are compatible for a metric.
	 * @return The compatible graph types.
	 */
	 public Set<GraphType> compatibleGraphTypes();
	
}
