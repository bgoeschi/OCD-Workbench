package i5.las2peer.services.ocd.graphs;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToOne;

import y.base.Node;

@Entity
@IdClass(CentralityMapId.class)
public class CentralityMap {
	/*
	 * Database column name definitions.
	 */
	public static final String graphIdColumnName = "GRAPH_ID";
	public static final String graphUserColumnName = "USER_NAME";
	public static final String idColumnName = "ID";
	private static final String creationMethodColumnName = "CREATION_METHOD";
	
	/*
	 * Field name definitions for JPQL queries.
	 */
	public static final String GRAPH_FIELD_NAME = "graph";
	public static final String CREATION_METHOD_FIELD_NAME  = "creationMethod";
	public static final String ID_FIELD_NAME = "id";
	
	/**
	 * System generated persistence id.
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = idColumnName)
	private long id;
	/**
	 * The graph that the CentralityMap is based on.
	 */
	@Id
	@JoinColumns( {
		@JoinColumn(name = graphIdColumnName, referencedColumnName = CustomGraph.idColumnName),
		@JoinColumn(name = graphUserColumnName, referencedColumnName = CustomGraph.userColumnName)
	})
	private CustomGraph graph;
	
	/**
	 * Logged data about the algorithm that created the CentralityMap.
	 */
	@OneToOne(orphanRemoval = true, cascade={CascadeType.ALL})
	@JoinColumn(name = creationMethodColumnName)
	private CentralityCreationLog creationMethod = new CentralityCreationLog(CentralityCreationType.UNDEFINED, new HashSet<GraphType>());

	private Map<Integer, Double> map = new HashMap<Integer, Double>();
	
	/**
	 * Creates a new instance.
	 * Only for persistence purposes.
	 */
	protected CentralityMap() {
		
	}
	
	public CentralityMap(CustomGraph graph) {
		this.graph = graph;
	}
	
	/**
	 * Getter for the id.
	 * @return The id.
	 */
	public long getId() {
		return id;
	}
	
	/**
	 * Getter for the graph that the CentralityMap is based on.
	 * @return The graph.
	 */
	public CustomGraph getGraph() {
		return graph;
	}
	
	public Map<Integer, Double> getMap() {
		return map;
	}
	
	public void setMap(Map<Integer, Double> map) {
		this.map = map;
	}
	
	public void setNodeValue(Node node, double value) {
		if(graph.contains(node)) {
			map.put(node.index(), value);
		}
	}
	
	public double getNodeValue(Node node) {
		return map.get(node.index());
	}
	
	public double getNodeIndexValue(int index) {
		return map.get(index);
	}
	
	/**
	 * Getter for the CentralityMap creation method.
	 * @return The creation method.
	 */
	public CentralityCreationLog getCreationMethod() {
		return creationMethod;
	}

	/**
	 * Setter for the CentralityMap creation method.
	 * @param creationMethod The creation method.
	 */
	public void setCreationMethod(CentralityCreationLog creationMethod) {
		if(creationMethod != null) {
			this.creationMethod = creationMethod;
		}
		else {
			this.creationMethod = new CentralityCreationLog(CentralityCreationType.UNDEFINED, new HashSet<GraphType>());
		}
	}

	@Override
	public String toString() {
		return "map=" + map;
	}
}
