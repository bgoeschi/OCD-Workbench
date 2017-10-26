package i5.las2peer.services.ocd.algorithms;

import java.io.File;
//import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.lang3.SystemUtils;
import org.la4j.matrix.Matrix;
import org.la4j.matrix.sparse.CCSMatrix;

import java.util.Scanner;

import i5.las2peer.services.ocd.algorithms.utils.OcdAlgorithmException;
import i5.las2peer.services.ocd.graphs.Cover;
import i5.las2peer.services.ocd.graphs.CoverCreationType;
import i5.las2peer.services.ocd.graphs.CustomGraph;
import i5.las2peer.services.ocd.graphs.GraphType;
import y.base.Edge;
import y.base.EdgeCursor;
import y.base.Node;
import y.base.NodeCursor;

/*
 * @author YLi
 */
public class EvolutionaryAlgorithmBasedOnSimilarity implements OcdAlgorithm {

	/*
	 * Path of the directory reserved for the application.
	 */
	private static final String DirectoryPath = "ocd/mea/";
	/*
	 * Used for synchronization purposes. Executes the application execution.
	 */
	private static DefaultExecutor executor = new DefaultExecutor();
	/*
	 * Path of the application for Linux based on the source codes of Liu et al.
	 */
	private static String linuxMeaApplicationPath = "./MeaLinux";
	/*
	 * Path of the application for Windows based on the source codes of Liu et
	 * al.
	 */
	private static String windowsMeaApplicationPath = DirectoryPath + "MeaWindows.exe";
	/*
	 * Path of paj file.
	 */
	private static final String graphPath = DirectoryPath + "network.paj";
	private static final String graphName = "network.paj";
	/*
	 * Path of the output of last generation.
	 */
	private static String LastResultPath = DirectoryPath + "network.paj.0099.pop";

	private int minNodeIndex = 0;

	@Override
	public CoverCreationType getAlgorithmType() {
		return CoverCreationType.EVOLUTIONARY_ALGORITHM_BASED_ON_SIMILARITY;
	}

	@Override
	public void setParameters(Map<String, String> parameters) {
	}

	@Override
	public Map<String, String> getParameters() {
		return new HashMap<String, String>();
	}

	@Override
	public Set<GraphType> compatibleGraphTypes() {
		Set<GraphType> compatibilities = new HashSet<GraphType>();
		compatibilities.add(GraphType.NEGATIVE_WEIGHTS);
		compatibilities.add(GraphType.WEIGHTED);
		return compatibilities;
	}

	/**
	 * Creates a standard instance of the algorithm. All attributes are assigned
	 * their default values.
	 */
	public EvolutionaryAlgorithmBasedOnSimilarity() {
	}

	@Override
	public Cover detectOverlappingCommunities(CustomGraph graph) throws OcdAlgorithmException, InterruptedException {
		synchronized (executor) {
			try {
				String executorFilename;
				if (SystemUtils.IS_OS_LINUX) {
					executorFilename = linuxMeaApplicationPath;
				} else if (SystemUtils.IS_OS_WINDOWS) {
					executorFilename = windowsMeaApplicationPath;
				}
				/*
				 * Benchmark not implemented for this operating system.
				 */
				else {
					throw new OcdAlgorithmException();
				}

				/*
				 * The source code of this algorithm requires the first node of
				 * the network file to have the index 1. If not, the input file
				 * should be adapted.
				 */
				NodeCursor nodes = graph.nodes();
				Node node;
				int count = 0;
				while (nodes.ok()) {
					node = nodes.node();
					if (count == 0) {
						minNodeIndex = node.index();
					} else {
						if (node.index() < minNodeIndex) {
							minNodeIndex = node.index();
						}
					}
					count++;
					nodes.next();
				}
				writeNetworkFile(graph);
				CommandLine cmdLine = new CommandLine(executorFilename);
				cmdLine.addArgument(graphName);
				File workingDirectory = new File(DirectoryPath);
				executor.setWorkingDirectory(workingDirectory);
				DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
				executor.execute(cmdLine, resultHandler);
				resultHandler.waitFor();
				if (resultHandler.getExitValue() != 0) {
					System.out.println(resultHandler.getException());
					throw new Exception("MEA Process exit value: " + resultHandler.getExitValue());
				}
				int nodeCount = graph.nodeCount();
				Matrix membershipMatrix = translateCommunityFile(LastResultPath, nodeCount);
				Cover cover = new Cover(graph, membershipMatrix);
				return cover;
			} catch (InterruptedException e) {
				throw e;
			} catch (Exception e) {
				e.printStackTrace();
				if (Thread.interrupted()) {
					throw new InterruptedException();
				}
				throw new OcdAlgorithmException(e);
			}

		}
	}

	// translate graph into paj file
	protected void writeNetworkFile(CustomGraph graph) throws IOException, InterruptedException {
		FileWriter networkFile = new FileWriter(graphPath);
		try {
			networkFile.write(String.format("*Vertices "));
			networkFile.write("\t");
			networkFile.write(Integer.toString(graph.nodeCount()));
			networkFile.write(System.lineSeparator());
			networkFile.write(String.format("*Edges"));
			networkFile.write(System.lineSeparator());
			EdgeCursor edges = graph.edges();
			Edge edge;
			while (edges.ok()) {
				if (Thread.interrupted()) {
					throw new InterruptedException();
				}
				edge = edges.edge();
				if (edge.source().index() <= edge.target().index()) {
					networkFile.write(Integer.toString(edge.source().index() + (1 - minNodeIndex)));// networkFile.write(Integer.toString(edge.source().index()+1));
					networkFile.write("\t");
					networkFile.write(Integer.toString(edge.target().index() + (1 - minNodeIndex)));// networkFile.write(Integer.toString(edge.source().index()+1));
					networkFile.write("\t");
					networkFile.write(Double.toString(graph.getEdgeWeight(edge)));
					networkFile.write(System.lineSeparator());
				}
				edges.next();
			}
		} finally {
			networkFile.close();
		}
	}

	//Transform the results written by the executable file into the membership matrix.
	protected Matrix translateCommunityFile(String LastResultPath, int nodeCount)
			throws IOException, InterruptedException {
		File resultFile = new File(LastResultPath);
		Scanner communityResult = new Scanner(resultFile);
		List<List<Integer>> membership = new ArrayList<List<Integer>>();
		Map<Integer, List<Integer>> membershipTranspose = new HashMap<Integer, List<Integer>>();
		Integer communitySize;
		try {
			while (communityResult.hasNext()) {
				if (communityResult.next().equals("#community")) {
					communityResult.next();
					communityResult.next();
					communitySize = communityResult.nextInt();
					List<Integer> memberList = new ArrayList<Integer>();
					memberList.clear();
					for (int i = 0; i < communitySize; i++) {

						memberList.add(communityResult.nextInt());

					}
					membership.add(memberList);
				}

			}
			int communityCount = membership.size();
			for (int i = 0; i < communityCount; i++) {
				Integer community = i;
				for (Integer element : membership.get(i)) {

					List<Integer> communityList = new ArrayList<Integer>();
					if (membershipTranspose.keySet().contains(element)) {
						membershipTranspose.get(element).add(community);
					} else {
						communityList.add(community);
						membershipTranspose.put(element, communityList);
					}
				}
			}
			Matrix membershipMatrix = new CCSMatrix(nodeCount, membership.size());
			for (int i = 0; i < nodeCount; i++) {
				double entryInTheLine = (double) 1 / (double) membershipTranspose.get(i).size();
				for (int j = 0; j < membershipTranspose.get(i).size(); j++) {
					membershipMatrix.set(i, membershipTranspose.get(i).get(j), entryInTheLine);
				}
			}
			return membershipMatrix;
		}

		catch (Exception e) {
			throw new IOException(e);
		} finally {
			try {
				communityResult.close();
			} catch (Exception e) {
			}
		}
	}
}
