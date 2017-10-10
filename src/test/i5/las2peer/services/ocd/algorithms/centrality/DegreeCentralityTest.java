package i5.las2peer.services.ocd.algorithms.centrality;

import org.junit.Test;

import i5.las2peer.services.ocd.centrality.measures.DegreeCentrality;
import i5.las2peer.services.ocd.graphs.CentralityMap;
import i5.las2peer.services.ocd.graphs.CustomGraph;
import y.base.Node;

public class DegreeCentralityTest {
	@Test
	public void testDegreeCentrality() throws InterruptedException {
		CustomGraph graph = new CustomGraph();
		
		Node nodes[] = new Node[5];  
		for (int i = 0; i < 5; i++) {
			nodes[i] = graph.createNode();
		}
		
		graph.createEdge(nodes[0], nodes[2]);
		graph.createEdge(nodes[1], nodes[2]);
		graph.createEdge(nodes[2], nodes[3]);
		graph.createEdge(nodes[3], nodes[4]);
		
		DegreeCentrality d = new DegreeCentrality();
		CentralityMap map = d.getValues(graph);
		
		for(Node n : nodes) {
			System.out.println(map.getNodeValue(n));
		}
		
		map = d.getValues(graph);
		System.out.println("Normalized:");
		for(Node n : nodes) {
			System.out.println(map.getNodeValue(n));
		}
	}
}
