package i5.las2peer.services.ocd.algorithms.centrality;

import org.junit.Test;

import i5.las2peer.services.ocd.graphs.CustomGraph;
import y.base.Node;

public class DegreeCentralityTest {
	@Test
	public void testDegreeCentrality() {
		CustomGraph graph = new CustomGraph();
		
		Node n[] = new Node[5];  
		for (int i = 0; i < 5; i++) {
			n[i] = graph.createNode();
		}
		
		graph.createEdge(n[0], n[2]);
		graph.createEdge(n[1], n[2]);
		graph.createEdge(n[2], n[3]);
		graph.createEdge(n[3], n[4]);
		
		DegreeCentrality d = new DegreeCentrality();
		double[] values = d.getValues(graph);
		
		for(double value : values) {
			System.out.println(value);
		}
		
		values = d.getNormalizedValues(graph);
		System.out.println("Normalized:");
		for(double value : values) {
			System.out.println(value);
		}
	}
}
