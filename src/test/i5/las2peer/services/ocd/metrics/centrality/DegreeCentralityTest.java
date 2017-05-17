package i5.las2peer.services.ocd.metrics.centrality;

import org.junit.Test;

import y.base.Graph;
import y.base.Node;

public class DegreeCentralityTest {
	@Test
	public void testDegreeCentrality() {
		Graph graph = new Graph();
		
		Node n[] = new Node[5];  
		for (int i = 0; i < 5; i++) {
			n[i] = graph.createNode();
		}
		
		graph.createEdge(n[0], n[2]);
		graph.createEdge(n[1], n[2]);
		graph.createEdge(n[2], n[3]);
		graph.createEdge(n[3], n[4]);
		
		DegreeCentrality d = new DegreeCentrality(graph);
		double[] values = d.getValues();
		
		for(double value : values) {
			System.out.println(value);
		}
		
		values = d.getNormalizedValues();
		System.out.println("Normalized:");
		for(double value : values) {
			System.out.println(value);
		}
	}
}
