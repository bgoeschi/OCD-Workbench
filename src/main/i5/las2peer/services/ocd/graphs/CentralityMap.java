package i5.las2peer.services.ocd.graphs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import i5.las2peer.services.ocd.metrics.centrality.CentralityMetricLog;
import i5.las2peer.services.ocd.metrics.centrality.CentralityMetricType;
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
	public static final String METRICS_FIELD_NAME = "metrics";
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
	/**
	 * The metric logs calculated for the cover.
	 */
	@OneToMany(mappedBy = "map", orphanRemoval = true, cascade={CascadeType.ALL})
	private List<CentralityMetricLog> metrics = new ArrayList<CentralityMetricLog>();
	
	private Map<String, Double> map = new HashMap<String, Double>();
	
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
	
	public Map<String, Double> getMap() {
		return map;
	}
	
	public void setMap(Map<String, Double> map) {
		this.map = map;
	}
	
	public void setNodeValue(Node node, double value) {
		if(graph.contains(node)) {
			map.put(graph.getNodeName(node), value);
		}
	}
	
	public double getNodeValue(Node node) {
		return map.get(graph.getNodeName(node));
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
	
	/**
	 * Getter for the metric logs calculated for the CentralityMap.
	 * @return The metric logs.
	 */
	public List<CentralityMetricLog> getMetrics() {
		return metrics;
	}

	/**
	 * Setter for the metric logs calculated for the CentralityMap.
	 * @param metrics The metric logs.
	 */
	public void setMetrics(List<CentralityMetricLog> metrics) {
		this.metrics.clear();
		for(CentralityMetricLog metric : metrics) {
			if(metric != null)
				this.metrics.add(metric);
		}
	}

	/**
	 * Returns the first metric occurrence with the corresponding metric type.
	 * @param metricType The metric type.
	 * @return The metric. Null if no such metric exists.
	 */
	public CentralityMetricLog getMetric(CentralityMetricType metricType) {
		for(CentralityMetricLog metric : this.metrics){
			if(metricType == metric.getType()) {
				return metric;
			}
		}
		return null;
	}
	
	/**
	 * Adds a metric log to the CentralityMap.
	 * @param metric The metric log.
	 */
	public void addMetric(CentralityMetricLog metric) {
		if(metric != null) {
			this.metrics.add(metric);
		}
	}
	
	/**
	 * Removes a metric log from the CentralityMap.
	 * @param metric The metric log.
	 */
	public void removeMetric(CentralityMetricLog metric) {
		this.metrics.remove(metric);
	}

	@Override
	public String toString() {
		return "map=" + map;
	}
}
