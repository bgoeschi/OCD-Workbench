package i5.las2peer.services.ocd;

import i5.las2peer.p2p.LocalNode;
import i5.las2peer.p2p.NodeException;
import i5.las2peer.p2p.ServiceNameVersion;
import i5.las2peer.persistency.MalformedXMLException;
import i5.las2peer.security.AgentException;
import i5.las2peer.security.L2pSecurityException;
import i5.las2peer.security.ServiceAgent;
import i5.las2peer.security.UserAgent;
import i5.las2peer.services.ocd.adapters.AdapterException;
import i5.las2peer.services.ocd.graphs.CustomGraph;
import i5.las2peer.services.ocd.testsUtils.OcdTestGraphFactory;
import i5.las2peer.services.ocd.utils.RequestHandler;
import i5.las2peer.testing.MockAgentFactory;
import i5.las2peer.tools.CryptoException;
import i5.las2peer.webConnector.WebConnector;
import i5.las2peer.webConnector.client.ClientResponse;
import i5.las2peer.webConnector.client.MiniClient;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test Class testing the Service calls
 * 
 */


public class ServiceTest {

	private static final String HTTP_ADDRESS = "http://127.0.0.1";
	private static final int HTTP_PORT = WebConnector.DEFAULT_HTTP_PORT;

	private static LocalNode serviceNode;
	private static WebConnector connector;
	private static ByteArrayOutputStream logStream;

	private static UserAgent adam;
	private static final String testPass = "adamspass";

	private static ServiceAgent testServiceAgent;	
	private static final String testServiceClass = "i5.las2peer.services.ocd.ServiceClass";
	private static final String mainPath = "ocd/";
	private static long SawmillGraphId;
	private static long DolphinsGraphId;
	private static long AperiodicTwoCommunitiesGraphId;
	
	private static RequestHandler requestHandler = new RequestHandler();
	
	/**
	 * Called before the tests start.
	 * 
	 * Sets up the node and initializes connector and users that can be used
	 * throughout the tests.
	 * 
	 * @throws Exception
	 */
	@BeforeClass
	public static void startServer() throws Exception {

		// start node
		serviceNode = LocalNode.newNode();
		serviceNode.getNodeServiceCache().setWaitForResults(3);
		
		adam = MockAgentFactory.getAdam();
		adam.unlockPrivateKey(testPass); 
		
		serviceNode.storeAgent(adam);
		serviceNode.launch();

		testServiceAgent = ServiceAgent.createServiceAgent(ServiceNameVersion.fromString(testServiceClass + "@1.0"), "a pass");
		testServiceAgent.unlockPrivateKey("a pass");
		serviceNode.registerReceiver(testServiceAgent);

		// start connector
		logStream = new ByteArrayOutputStream();

		connector = new WebConnector(true, HTTP_PORT, false, 1000);
		connector.setLogStream(new PrintStream(logStream));
		connector.start(serviceNode);
		Thread.sleep(1000); // wait a second for the connector to become ready
		adam = MockAgentFactory.getAdam();
		
		/*
		 * Sets up the database environment for testing.
		 */
		
		setupDatabase();

	}

	/*
	 * Sets up the database environment for testing.
	 */
	private static void setupDatabase() throws AdapterException,
			FileNotFoundException, ParserConfigurationException {
		/*
		 * Set db content
		 */
		CustomGraph graph;
		graph = OcdTestGraphFactory.getAperiodicTwoCommunitiesGraph();
		createGraph(graph);
		AperiodicTwoCommunitiesGraphId = graph.getId();
		
		graph = OcdTestGraphFactory.getDolphinsGraph();
		createGraph(graph);
		DolphinsGraphId = graph.getId();
		
		graph = OcdTestGraphFactory.getSawmillGraph();
		createGraph(graph);
		SawmillGraphId = graph.getId();
	}

	// Persists a graph for database setup.
	public static void createGraph(CustomGraph graph) throws AdapterException,
			FileNotFoundException, ParserConfigurationException {
		
		graph.setUserName(adam.getLoginName());
		EntityManager em = requestHandler.getEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			em.persist(graph);
			tx.commit();
		} catch (RuntimeException e) {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
			throw e;
		}
		em.close();
		System.out.println(requestHandler.writeId(graph));
	}

	/**
	 * Called after the tests have finished. Shuts down the server and prints
	 * out the connector log file for reference.
	 * 
	 * @throws Exception
	 */
	@AfterClass
	public static void shutDownServer() throws Exception {

		connector.stop();
		serviceNode.shutDown();

		connector = null;
		serviceNode = null;

		LocalNode.reset();

		System.out.println("Connector-Log:");
		System.out.println("--------------");

		System.out.println(logStream.toString());

	}

	/**
	 * 
	 * Tests the validate method.
	 * 
	 */
	@Test
	public void testValidateLogin() {
		MiniClient c = new MiniClient();
		c.setAddressPort(HTTP_ADDRESS, HTTP_PORT);

		try {
			c.setLogin(Long.toString(adam.getId()), testPass);
			ClientResponse result = c.sendRequest("GET", mainPath + "validate",
					"");
			assertEquals(200, result.getHttpCode());
			System.out.println("Result of 'testValidateLogin': "
					+ result.getResponse().trim());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception: " + e);
		}

	}
	
	
	////////// Test RESTful Service calls  //////////// 
	
	
	@Test
	public void testGetGraph() throws AdapterException, FileNotFoundException {
		MiniClient c = new MiniClient();
		c.setAddressPort(HTTP_ADDRESS, HTTP_PORT);
		try {
			c.setLogin(Long.toString(adam.getId()), testPass);
			ClientResponse result = c.sendRequest("GET", mainPath + "graphs/"
					+ SawmillGraphId + "?outputFormat=META_XML", "");
			assertEquals(200, result.getHttpCode());
			System.out.println("Result of 'testGetGraphs' on Sawmill: "
					+ result.getResponse().trim());
			result = c.sendRequest("GET", mainPath + "graphs/" + DolphinsGraphId
					+ "?outputFormat=META_XML", "");
			assertEquals(200, result.getHttpCode());
			System.out.println("Result of 'testGetGraphs' on Dolphins: "
					+ result.getResponse().trim());
			result = c.sendRequest("GET",
					mainPath + "graphs/" + AperiodicTwoCommunitiesGraphId
							+ "?outputFormat=META_XML", "");
			assertEquals(200, result.getHttpCode());
			System.out
					.println("Result of 'testGetGraphs' on AperiodicTwoCommunities: "
							+ result.getResponse().trim());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception: " + e);
		}
	}
	
	
	
	//////////Test RMI functions  //////////// 
	
	@Test
	public void testGetRMIGraph() throws AdapterException, MalformedXMLException, IOException, L2pSecurityException, AgentException, NodeException, CryptoException, InterruptedException {
		
	
		LocalNode serviceNode = LocalNode.newNode();
		serviceNode.getNodeServiceCache().setWaitForResults(3);
		UserAgent adam = MockAgentFactory.getAdam();

		adam.unlockPrivateKey("adamspass");
		serviceNode.launch();

		ServiceAgent testServiceAgent = ServiceAgent
				.createServiceAgent(ServiceNameVersion.fromString(testServiceClass + "@1.0"), "a pass");
		testServiceAgent.unlockPrivateKey("a pass");
		serviceNode.registerReceiver(testServiceAgent);
		
		
		double[][] matrix = (double[][]) serviceNode.invoke(adam, ServiceNameVersion.fromString(testServiceClass) + "@1.0", "getGraphAsAdjacencyMatrix", new Serializable[] { SawmillGraphId });

		assertEquals(36, matrix.length);
		assertEquals(1, 1, matrix[0][1]);
		assertEquals(0, 0, matrix[3][8]);
		assertEquals(1, 1, matrix[16][19]);
		assertEquals(1, 1, matrix[19][16]);
		assertEquals(1, 1, matrix[34][35]);
		
		
	}
		
		
		
	

}
