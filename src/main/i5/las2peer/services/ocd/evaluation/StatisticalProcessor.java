package i5.las2peer.services.ocd.evaluation;

import java.util.List;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;

import i5.las2peer.services.ocd.graphs.CentralityMap;
import i5.las2peer.services.ocd.graphs.CustomGraph;
import y.base.Node;
import y.base.NodeCursor;

public class StatisticalProcessor {
	/**
	 * Create a new centrality map containing the average values of the centrality maps in the given list.
	 * @param graph The graph the centrality measures are based on.
	 * @param maps The list of centrality maps.
	 * @return The resulting average centrality map.
	 */
	public static CentralityMap getAverageMap(CustomGraph graph, List<CentralityMap> maps) {
		CentralityMap resultMap = new CentralityMap(graph);
		int mapListSize = maps.size();
		
		NodeCursor nc = graph.nodes();
		while(nc.ok()) {
			Node currentNode = nc.node();
			double currentNodeAverage = 0.0;
			for(CentralityMap currentMap : maps) {
				currentNodeAverage += currentMap.getNodeValue(graph.getNodeName(currentNode));
			}
			resultMap.setNodeValue(currentNode, currentNodeAverage/mapListSize);
			nc.next();
		}
		return resultMap;
	}
	
	/**
	 * Calculates the Spearman correlation matrix for the centrality maps in the given list.
	 * @param graph The graph the centrality measures are based on.
	 * @param maps The list of centrality maps.
	 * @return The correlation matrix as a RealMatrix.
	 */
	public static RealMatrix spearmanCorrelation(CustomGraph graph, List<CentralityMap> maps) {
		int n = graph.nodeCount();
		int m = maps.size();
		double[][] mapsValues = new double[n][m];
		NodeCursor nc = graph.nodes();
		int i = 0;
		while(nc.ok()) {
			Node currentNode = nc.node();
			for(int j = 0; j < m; j++) {
				mapsValues[i][j] = maps.get(j).getNodeValue(graph.getNodeName(currentNode));
			}
			nc.next();
			i++;
		}	
		SpearmansCorrelation correlation = new SpearmansCorrelation();
		RealMatrix result = correlation.computeCorrelationMatrix(mapsValues);	
		return result;
	}
}
