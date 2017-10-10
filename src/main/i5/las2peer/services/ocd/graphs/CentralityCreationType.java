package i5.las2peer.services.ocd.graphs;

import java.security.InvalidParameterException;
import java.util.Locale;

import i5.las2peer.services.ocd.centrality.measures.CentralityAlgorithm;

/**
 * Analogous to CoverCreationType but for centrality algorithms.
 * @author Tobias
 *
 */
public enum CentralityCreationType {
	
	UNDEFINED(CentralityCreationMethod.class, 0),
	
	/**
	 * Abstract type for ground-truth rankings
	 */
	GROUND_TRUTH(CentralityCreationMethod.class, 1),
	
	/**
	 * Type corresponding to simulations, e.g. of the spreading influence of nodes
	 */
	SIMULATION(CentralityCreationMethod.class, 2),
	
	/**
	 * Type corresponding to averaging of centrality values
	 */
	AVERAGE(CentralityCreationMethod.class, 3),
	
	/**
	 * Type corresponding to degree centrality
	 */
	DEGREE_CENTRALITY(i5.las2peer.services.ocd.centrality.measures.DegreeCentrality.class, 4),
	
	/**
	 * Type corresponding to in-degree
	 */
	IN_DEGREE(i5.las2peer.services.ocd.centrality.measures.InDegree.class, 5),
	
	/**
	 * Type corresponding to out-degree
	 */
	OUT_DEGREE(i5.las2peer.services.ocd.centrality.measures.OutDegree.class, 6),
	
	/**
	 * Type corresponding to LocalRank
	 */
	LOCAL_RANK(i5.las2peer.services.ocd.centrality.measures.LocalRank.class, 7),
	
	/**
	 * Type corresponding to ClusterRank
	 */
	CLUSTER_RANK(i5.las2peer.services.ocd.centrality.measures.ClusterRank.class, 8),
	
	/**
	 * Type corresponding to Corensess
	 */
	CORENESS(i5.las2peer.services.ocd.centrality.measures.Coreness.class, 9),
	
	/**
	 * Type corresponding to the neighborhood coreness
	 */
	NEIGHBORHOOD_CORENESS(i5.las2peer.services.ocd.centrality.measures.NeighborhoodCoreness.class, 10),
	
	/**
	 * Type corresponding to H-Index
	 */
	H_INDEX(i5.las2peer.services.ocd.centrality.measures.HIndex.class, 11),
	
	/**
	 * Type corresponding to eccentricity
	 */
	ECCENTRICITY(i5.las2peer.services.ocd.centrality.measures.Eccentricity.class, 12),
	
	/**
	 * Type corresponding to closeness centrality
	 */
	CLOSENESS_CENTRALITY(i5.las2peer.services.ocd.centrality.measures.ClosenessCentrality.class, 13),
	
	/**
	 * Type corresponding to harmonic centrality
	 */
	HARMONIC_CENTRALITY(i5.las2peer.services.ocd.centrality.measures.HarmonicCentrality.class, 14),
	
	/**
	 * Type corresponding to the current flow closeness centrality
	 */
	CURRENT_FLOW_CLOSENESS(i5.las2peer.services.ocd.centrality.measures.CurrentFlowCloseness.class, 15),
	
	/**
	 * Type corresponding to integration
	 */
	INTEGRATION(i5.las2peer.services.ocd.centrality.measures.Integration.class, 16),
	
	/**
	 * Type corresponding to radiality
	 */
	RADIALITY(i5.las2peer.services.ocd.centrality.measures.Radiality.class, 17),
	
	/**
	 * Type corresponding to the residual closeness
	 */
	RESIDUAL_ClOSENESS(i5.las2peer.services.ocd.centrality.measures.ResidualCloseness.class, 18),
	
	/**
	 * Type corresponding to the centroid value
	 */
	CENTROID_VALUE(i5.las2peer.services.ocd.centrality.measures.CentroidValue.class, 19),
	
	/**
	 * Type corresponding to the stress centrality
	 */
	STRESS_CENTRALITY(i5.las2peer.services.ocd.centrality.measures.StressCentrality.class, 20),
	
	/**
	 * Type corresponding to betweenness centrality
	 */
	BETWEENNESS_CENTRALITY(i5.las2peer.services.ocd.centrality.measures.BetweennessCentrality.class, 21),
	
	/**
	 * Type corresponding to the current flow betweenness centrality
	 */
	CURRENT_FLOW_BETWEENNESS(i5.las2peer.services.ocd.centrality.measures.CurrentFlowBetweenness.class, 22),
	
	/**
	 * Type corresponding to flow betweenness
	 */
	FLOW_BETWEENNESS(i5.las2peer.services.ocd.centrality.measures.FlowBetweenness.class, 23),
	
	/**
	 * Type corresponding to the bridging coefficient
	 */
	BRIDGING_COEFFICIENT(i5.las2peer.services.ocd.centrality.measures.BridgingCoefficient.class, 24),
	
	/**
	 * Type corresponding to the bridging centrality
	 */
	BRIDGING_CENTRALITY(i5.las2peer.services.ocd.centrality.measures.BridgingCentrality.class, 25),
	
	/**
	 * Type corresponding to Katz centrality
	 */
	KATZ_CENTRALITY(i5.las2peer.services.ocd.centrality.measures.KatzCentrality.class, 26),
	
	/**
	 * Type corresponding to subgraph centrality
	 */
	SUBGRAPH_CENTRALITY(i5.las2peer.services.ocd.centrality.measures.SubgraphCentrality.class, 27),
	
	/**
	 * Type corresponding to eigenvector centrality
	 */
	EIGENVECTOR_CENTRALITY(i5.las2peer.services.ocd.centrality.measures.EigenvectorCentrality.class, 28),
	
	/**
	 * Type corresponding to the alpha centrality
	 */
	ALPHA_CENTRALITY(i5.las2peer.services.ocd.centrality.measures.AlphaCentrality.class, 29),
	
	/**
	 * Type corresponding to the bargaining centrality
	 */
	BARGAINING_CENTRALITY(i5.las2peer.services.ocd.centrality.measures.BargainingCentrality.class, 30),
	
	/**
	 * Type corresponding to PageRank
	 */
	PAGERANK(i5.las2peer.services.ocd.centrality.measures.PageRank.class, 31),
	
	/**
	 * Type corresponding to alpha centrality
	 */
	LEADERRANK(i5.las2peer.services.ocd.centrality.measures.LeaderRank.class, 32),
	
	/**
	 * Type corresponding to the hyperlink-induced topic search (HITS) hub score
	 */
	HITS_HUB_SCORE(i5.las2peer.services.ocd.centrality.measures.HitsHubScore.class, 33),
	
	/**
	 * Type corresponding to the hyperlink-induced topic search (HITS) authority score
	 */
	HITS_AUTHORITY_SCORE(i5.las2peer.services.ocd.centrality.measures.HitsAuthorityScore.class, 34),
	
	/**
	 * Type corresponding to the SALSA hub score
	 */
	SALSA_HUB_SCORE(i5.las2peer.services.ocd.centrality.measures.SalsaHubScore.class, 35),
	
	/**
	 * Type corresponding to the SALSA authority score
	 */
	SALSA_AUTHORITY_SCORE(i5.las2peer.services.ocd.centrality.measures.SalsaAuthorityScore.class, 36);
	
	/**
	 * The class corresponding to the type
	 */
	private final Class<? extends CentralityCreationMethod> creationMethodClass;
	
	/**
	 * For persistence and other purposes.
	 */
	private final int id;
	
	/**
	 * Creates a new instance.
	 * @param creationMethodClass Defines the creationMethodClass attribute.
	 * @param id Defines the id attribute.
	 */
	private CentralityCreationType(Class<? extends CentralityCreationMethod> creationMethodClass, int id) {
		this.creationMethodClass = creationMethodClass;
		this.id = id;
	}
	
	/**
	 * Returns the CentralityCreationMethod subclass corresponding to the type.
	 * @return The corresponding class.
	 */
	public Class<? extends CentralityCreationMethod> getCreationMethodClass() {
		return this.creationMethodClass;
	}
	
	/**
	 * Returns the unique id of the type.
	 * @return The id.
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Returns the type corresponding to an id.
	 * @param id The id.
	 * @return The corresponding type.
	 */
	public static CentralityCreationType lookupType(int id) {
        for (CentralityCreationType type : CentralityCreationType.values()) {
            if (id == type.getId()) {
                return type;
            }
        }
        throw new InvalidParameterException();
	}
	
	/**
	 * States whether the corresponding creation method class is actually a CentralityAlgorithm.
	 * @return TRUE if the class is a CentralityAlgorithm, otherwise FALSE.
	 */
	public boolean correspondsAlgorithm() {
		if(CentralityAlgorithm.class.isAssignableFrom(this.getCreationMethodClass())) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Returns the name of the type written in lower case letters and with any underscores replaced by space characters.
	 */
	@Override
	public String toString() {
		String name = name();
		name = name.replace('_', ' ');
		name = name.toLowerCase(Locale.ROOT);
		return name;
	}
}
