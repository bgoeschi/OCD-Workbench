package i5.las2peer.services.ocd;

import i5.las2peer.services.ocd.algorithms.SskAlgorithm;
import i5.las2peer.services.ocd.graphs.Community;
import i5.las2peer.services.ocd.graphs.Cover;
import i5.las2peer.services.ocd.graphs.CustomGraph;
import y.base.Edge;
import y.base.Node;
import y.view.Graph2D;

/**
 * Created by bgoeschlberger on 26.10.2017.
 */
public class Main {
    public static void main(String... args) {
        CustomGraph cg = new CustomGraph();
        Node[] nodes = new Node[100];
        Edge[] edges = new Edge[200];
        for (int i = 0; i < 100; i++) {
            nodes[i] = cg.createNode();
            cg.setNodeName(nodes[i], "node " + (i + 1));
            int src = (int) Math.floor(Math.sqrt(i + 1)) - 1;
            if (i - src > 1) {
                edges[i] = cg.createEdge(nodes[src], nodes[i]);
                cg.setEdgeWeight(edges[i], i - src);
            }
            src = i - 1;
            if (src > 0) {
                edges[100 + i] = cg.createEdge(nodes[src], nodes[i]);
                cg.setEdgeWeight(edges[i], 1);
            }
        }
        SskAlgorithm sskAlgorithm = new SskAlgorithm();
        try {
            Cover cover = sskAlgorithm.detectOverlappingCommunities(cg);
            for (Community community : cover.getCommunities()) {
                System.out.println("Community [" + community.getId() + "]: size={" + community.getSize() + "} ("
                        + community.getMemberships().keySet().stream().map(node -> cg.getNodeName(node)).reduce((s, s2) -> s + ", " + s2).get() +")");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
