package i5.las2peer.services.ocd.algorithms.centrality;

import i5.las2peer.services.ocd.algorithms.utils.CentralityAlgorithmException;
import i5.las2peer.services.ocd.graphs.CentralityCreationLog;
import i5.las2peer.services.ocd.graphs.CentralityCreationType;
import i5.las2peer.services.ocd.graphs.CentralityMap;
import i5.las2peer.services.ocd.graphs.CustomGraph;
import i5.las2peer.services.ocd.graphs.GraphProcessor;
import i5.las2peer.services.ocd.metrics.ExecutionTime;
import i5.las2peer.services.ocd.utils.ExecutionStatus;

/**
 * Manages the execution of a CentralityAlgorithm.
 * @author Tobias
 *
 */
public class CentralityAlgorithmExecutor {

	/**
	 * Calculates a CentralityMap by executing an CentralityAlgorithm on a graph.
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
		ExecutionTime executionTime = new ExecutionTime();
		if(algorithm.getAlgorithmType() == CentralityCreationType.ECCENTRICITY || algorithm.getAlgorithmType() == CentralityCreationType.CLOSENESS_CENTRALITY || algorithm.getAlgorithmType() == CentralityCreationType.BETWEENNESS_CENTRALITY || algorithm.getAlgorithmType() == CentralityCreationType.INTEGRATION || algorithm.getAlgorithmType() == CentralityCreationType.RADIALITY) {
			processor.invertEdgeWeights(graphCopy);
		}
		if(algorithm.getAlgorithmType() == CentralityCreationType.RADIALITY) {
			processor.reverseEdgeDirections(graphCopy);
		}
		executionTime.start();
		CentralityMap map = algorithm.getValues(graphCopy);
		map.setCreationMethod(new CentralityCreationLog(algorithm.getAlgorithmType(), algorithm.compatibleGraphTypes()));
		map.getCreationMethod().setStatus(ExecutionStatus.COMPLETED);
		executionTime.stop();
		executionTime.setCentralityExecutionTime(map);
		return map;
	}
}
