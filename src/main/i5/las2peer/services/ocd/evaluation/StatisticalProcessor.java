package i5.las2peer.services.ocd.evaluation;

import java.util.List;

import i5.las2peer.services.ocd.graphs.CentralityMap;
import i5.las2peer.services.ocd.graphs.CustomGraph;
import y.base.Node;
import y.base.NodeCursor;

public class StatisticalProcessor {
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
}
