package i5.las2peer.services.ocd.metrics.centrality;

import i5.las2peer.services.ocd.graphs.CentralityMap;

public interface KnowledgeDrivenMeasure extends CentralityMetric {

	/**
	 * Measures a CentralityMap with respect to another ground truth map and adds 
	 * the resulting metric log to the CentralityMap.
	 * Implementations of this method must allow to be interrupted.
	 * I.e. they must periodically check the thread for interrupts
	 * and throw an InterruptedException if an interrupt was detected.
	 * @param map The CentralityMap which is evaluated. It must also contain the corresponding graph.
	 * @param groundTruth The ground truth map on whose bases the metric value is calculated.
	 * It must contain the same graph as the output map.
	 * @return The calculated metric value.
	 * @throws CentralityMetricException If the execution failed.
	 * @throws InterruptedException If the executing thread was interrupted.
	 */
	public double measure(CentralityMap map, CentralityMap groundTruth) throws CentralityMetricException, InterruptedException;
	
}
