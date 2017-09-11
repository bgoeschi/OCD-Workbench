package i5.las2peer.services.ocd.graphs;

import java.security.InvalidParameterException;
import java.util.Locale;

import i5.las2peer.services.ocd.algorithms.centrality.CentralityAlgorithm;

/**
 * Analogous to CoverCreationType but for centrality algorithms.
 * @author Tobias
 *
 */
public enum CentralityCreationType {
	
	UNDEFINED(CentralityCreationMethod.class, 0),
	
	/**
	 * Type corresponding to degree centrality
	 */
	DEGREE_CENTRALITY(i5.las2peer.services.ocd.algorithms.centrality.DegreeCentrality.class, 1),
	
	/**
	 * Type corresponding to in-degree
	 */
	IN_DEGREE(i5.las2peer.services.ocd.algorithms.centrality.InDegree.class, 2),
	
	/**
	 * Type corresponding to out-degree
	 */
	OUT_DEGREE(i5.las2peer.services.ocd.algorithms.centrality.OutDegree.class, 3),
	
	/**
	 * Type corresponding to eccentricity
	 */
	ECCENTRICITY(i5.las2peer.services.ocd.algorithms.centrality.Eccentricity.class, 4),
	
	/**
	 * Type corresponding to closeness centrality
	 */
	CLOSENESS_CENTRALITY(i5.las2peer.services.ocd.algorithms.centrality.ClosenessCentrality.class, 5),
	
	/**
	 * Type corresponding to LocalRank
	 */
	LOCAL_RANK(i5.las2peer.services.ocd.algorithms.centrality.LocalRank.class, 6),
	
	/**
	 * Type corresponding to ClusterRank
	 */
	CLUSTER_RANK(i5.las2peer.services.ocd.algorithms.centrality.ClusterRank.class, 7),
	
	/**
	 * Type corresponding to Corensess
	 */
	CORENESS(i5.las2peer.services.ocd.algorithms.centrality.Coreness.class, 8),
	
	/**
	 * Type corresponding to H-Index
	 */
	H_INDEX(i5.las2peer.services.ocd.algorithms.centrality.HIndex.class, 9),
	
	/**
	 * Type corresponding to harmonic centrality
	 */
	HARMONIC_CENTRALITY(i5.las2peer.services.ocd.algorithms.centrality.HarmonicCentrality.class, 10),
	
	/**
	 * Type corresponding to integration
	 */
	INTEGRATION(i5.las2peer.services.ocd.algorithms.centrality.Integration.class, 11),
	
	/**
	 * Type corresponding to radiality
	 */
	RADIALITY(i5.las2peer.services.ocd.algorithms.centrality.Radiality.class, 12),
	
	/**
	 * Type corresponding to betweenness centrality
	 */
	BETWEENNESS_CENTRALITY(i5.las2peer.services.ocd.algorithms.centrality.BetweennessCentrality.class, 13),
	
	/**
	 * Type corresponding to flow betweenness
	 */
	FLOW_BETWEENNESS(i5.las2peer.services.ocd.algorithms.centrality.FlowBetweenness.class, 14),
	
	/**
	 * Type corresponding to Katz centrality
	 */
	KATZ_CENTRALITY(i5.las2peer.services.ocd.algorithms.centrality.KatzCentrality.class, 15),
	
	/**
	 * Type corresponding to subgraph centrality
	 */
	SUBGRAPH_CENTRALITY(i5.las2peer.services.ocd.algorithms.centrality.SubgraphCentrality.class, 16),
	
	/**
	 * Type corresponding to eigenvector centrality
	 */
	EIGENVECTOR_CENTRALITY(i5.las2peer.services.ocd.algorithms.centrality.EigenvectorCentrality.class, 17),
	
	/**
	 * Type corresponding to PageRank
	 */
	PAGERANK(i5.las2peer.services.ocd.algorithms.centrality.PageRank.class, 18),
	
	/**
	 * Type corresponding to alpha centrality
	 */
	LEADERRANK(i5.las2peer.services.ocd.algorithms.centrality.LeaderRank.class, 19),
	
	/**
	 * Type corresponding to the hyperlink-induced topic search (HITS) hub score
	 */
	HITS_HUB_SCORE(i5.las2peer.services.ocd.algorithms.centrality.HitsHubScore.class, 20),
	
	/**
	 * Type corresponding to the hyperlink-induced topic search (HITS) authority score
	 */
	HITS_AUTHORITY_SCORE(i5.las2peer.services.ocd.algorithms.centrality.HitsAuthorityScore.class, 21),
	
	/**
	 * Type corresponding to the SALSA hub score
	 */
	SALSA_HUB_SCORE(i5.las2peer.services.ocd.algorithms.centrality.SalsaHubScore.class, 22),
	
	/**
	 * Type corresponding to the SALSA authority score
	 */
	SALSA_AUTHORITY_SCORE(i5.las2peer.services.ocd.algorithms.centrality.SalsaAuthorityScore.class, 23),
	
	/**
	 * Type corresponding to the bargaining centrality
	 */
	BARGAINING_CENTRALITY(i5.las2peer.services.ocd.algorithms.centrality.BargainingCentrality.class, 24),
	
	/**
	 * Type corresponding to the alpha centrality
	 */
	ALPHA_CENTRALITY(i5.las2peer.services.ocd.algorithms.centrality.AlphaCentrality.class, 25),
	
	/**
	 * Type corresponding to the current flow betweenness centrality
	 */
	CURRENT_FLOW_BETWEENNESS(i5.las2peer.services.ocd.algorithms.centrality.CurrentFlowBetweenness.class, 26),
	
	/**
	 * Type corresponding to the current flow closeness centrality
	 */
	CURRENT_FLOW_CLOSENESS(i5.las2peer.services.ocd.algorithms.centrality.CurrentFlowCloseness.class, 27),
	
	/**
	 * Type corresponding to the centroid value
	 */
	CENTROID_VALUE(i5.las2peer.services.ocd.algorithms.centrality.CentroidValue.class, 28),
	
	/**
	 * Type corresponding to the residual closeness
	 */
	RESIDUAL_ClOSENESS(i5.las2peer.services.ocd.algorithms.centrality.ResidualCloseness.class, 29);
	
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
