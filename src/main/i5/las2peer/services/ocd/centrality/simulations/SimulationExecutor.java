package i5.las2peer.services.ocd.centrality.simulations;

import i5.las2peer.services.ocd.centrality.measures.CentralityAlgorithmException;
import i5.las2peer.services.ocd.graphs.CentralityCreationLog;
import i5.las2peer.services.ocd.graphs.CentralityCreationType;
import i5.las2peer.services.ocd.graphs.CentralityMap;
import i5.las2peer.services.ocd.graphs.CustomGraph;
import i5.las2peer.services.ocd.graphs.GraphProcessor;
import i5.las2peer.services.ocd.utils.ExecutionStatus;
import y.base.Node;
import y.base.NodeCursor;

public class SimulationExecutor {
	
	/**
	 * Calculates a CentralityMap by running simulations on a graph.
	 * @param graph The graph.
	 * @param simulation The algorithm.
	 * @return A CentralityMap of the graph calculated by the algorithm.
	 * @throws CentralityAlgorithmException In case of an algorithm failure.
	 * @throws InterruptedException In case of an algorithm interrupt.
	 */
	public CentralityMap execute(CustomGraph graph, GraphSimulation simulation) throws CentralityAlgorithmException, InterruptedException {
		CustomGraph graphCopy = new CustomGraph(graph);
		GraphProcessor processor = new GraphProcessor();
		processor.makeCompatible(graphCopy, simulation.compatibleGraphTypes());
		long startTime = System.currentTimeMillis();
		CentralityMap map = new CentralityMap(graphCopy);
		map.setCreationMethod(new CentralityCreationLog(CentralityCreationType.GROUND_TRUTH, simulation.getParameters(), simulation.compatibleGraphTypes()));
		NodeCursor nc = graphCopy.nodes();
		while(nc.ok()) {
			if(Thread.interrupted()) {
				throw new InterruptedException();
			}
			Node currentNode = nc.node();
			if(simulation.getSimulationType() == SimulationType.SIR) {
				double d = ((SirSimulation) simulation).runSimulation(graphCopy, currentNode);
				map.setNodeValue(currentNode, d);
			}
			nc.next();
		}
		map.getCreationMethod().setStatus(ExecutionStatus.COMPLETED);
		long executionTime = System.currentTimeMillis() - startTime;
		map.getCreationMethod().setExecutionTime(executionTime);
		return map;
	}

}
