package i5.las2peer.services.servicePackage.adapters.coverInput;

import i5.las2peer.services.servicePackage.adapters.AdapterException;
import i5.las2peer.services.servicePackage.adapters.Adapters;
import i5.las2peer.services.servicePackage.graph.Cover;
import i5.las2peer.services.servicePackage.graph.CustomGraph;

import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.la4j.matrix.Matrix;
import org.la4j.matrix.sparse.CCSMatrix;

import y.base.Node;
import y.base.NodeCursor;

public class CommunityMemberListsInputAdapter extends AbstractCoverInputAdapter {

	CommunityMemberListsInputAdapter(Reader reader) {
		this.reader = reader;
	}
	
	@Override
	public Cover readCover(CustomGraph graph) throws AdapterException {
		String nodeName;
		String communityName;
		Map<String, List<Integer>> nodeCommunities = new HashMap<String,  List<Integer>>();
		List<Integer> communityIndices;
		Map<Integer, String> communityNames = new HashMap<Integer, String>();
		int communityCount = 0;
		try {
			List<String> line = Adapters.readLine(reader);
			/*
			 * Reads edges
			 */
			while (line.size() >= 2) {
				communityName = line.get(0);
				communityNames.put(communityCount, communityName);
				for(int i=1; i<line.size(); i++) {
					nodeName = line.get(i);
					if (!nodeCommunities.containsKey(nodeName)) {
						communityIndices = new ArrayList<Integer>();
						communityIndices.add(communityCount);
						nodeCommunities.put(nodeName, communityIndices);
					} else {
						nodeCommunities.get(nodeName).add(communityCount);
					}
				}
				communityCount++;
				line = Adapters.readLine(reader);
			}
		} catch (Exception e) {
			throw new AdapterException(e);
		} finally {
			try {
				reader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Matrix memberships = new CCSMatrix(graph.nodeCount(), communityCount);
		NodeCursor nodes = graph.nodes();
		Node node;
		while(nodes.ok()) {
			node = nodes.node();
			nodeName = graph.getNodeName(node);
			communityIndices = nodeCommunities.get(nodeName);
			if(communityIndices != null) {
				for(int communityIndex : communityIndices) {
					memberships.set(node.index(), communityIndex, 1d/communityIndices.size());
				}
			}
			nodes.next();
		}
		Cover cover = new Cover(graph, memberships);
		for(int i : communityNames.keySet()) {
			cover.setCommunityName(i, communityNames.get(i));
		}
		return cover;
	}

}
