package i5.las2peer.services.ocd.algorithms.centrality;

import i5.las2peer.services.ocd.algorithms.utils.CentralityAlgorithmException;
import i5.las2peer.services.ocd.graphs.CentralityCreationLog;
import i5.las2peer.services.ocd.graphs.CentralityCreationType;
import i5.las2peer.services.ocd.graphs.CentralityMap;
import i5.las2peer.services.ocd.graphs.CustomGraph;
import i5.las2peer.services.ocd.graphs.GraphProcessor;
import i5.las2peer.services.ocd.utils.ExecutionStatus;

/**
 * Manages the execution of a CentralityAlgorithm.
 * @author Tobias
 *
 */
public class CentralityAlgorithmExecutor {

	/**
	 * Calculates a CentralityMap by executing a CentralityAlgorithm on a graph.
	 * @param graph The graph.
	 * @param algorithm The algorithm.
	 * @return A CentralityMap of the graph calculated by the algorithm.
	 * @throws CentralityAlgorithmException In case of an algorithm failure.
	 * @throws InterruptedException In case of an algorithm interrupt.
	 */
	public CentralityMap execute(CustomGraph graph, CentralityAlgorithm algorithm) throws CentralityAlgorithmException, InterruptedException {
		CustomGraph graphCopy = new CustomGraph(graph);
		GraphProcessor processor = new GraphProcessor();
		processor.makeCompatible(graphCopy, algorithm.compatibleGraphTypes());
		if(algorithm.getAlgorithmType() == CentralityCreationType.ECCENTRICITY || algorithm.getAlgorithmType() == CentralityCreationType.CLOSENESS_CENTRALITY || algorithm.getAlgorithmType() == CentralityCreationType.BETWEENNESS_CENTRALITY || algorithm.getAlgorithmType() == CentralityCreationType.STRESS_CENTRALITY || algorithm.getAlgorithmType() == CentralityCreationType.INTEGRATION || algorithm.getAlgorithmType() == CentralityCreationType.RADIALITY || algorithm.getAlgorithmType() == CentralityCreationType.RESIDUAL_ClOSENESS) {
			processor.invertEdgeWeights(graphCopy);
		}
		if(algorithm.getAlgorithmType() == CentralityCreationType.RADIALITY) {
			processor.reverseEdgeDirections(graphCopy);
		}
		long startTime = System.currentTimeMillis();
		CentralityMap map = algorithm.getValues(graphCopy);
		map.setCreationMethod(new CentralityCreationLog(algorithm.getAlgorithmType(), algorithm.getParameters(), algorithm.compatibleGraphTypes()));
		map.getCreationMethod().setStatus(ExecutionStatus.COMPLETED);
		long executionTime = System.currentTimeMillis() - startTime;
		map.getCreationMethod().setExecutionTime(executionTime);
		return map;
	}
}
