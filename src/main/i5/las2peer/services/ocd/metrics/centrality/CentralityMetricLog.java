package i5.las2peer.services.ocd.metrics.centrality;

import i5.las2peer.services.ocd.graphs.CentralityMap;
import i5.las2peer.services.ocd.graphs.Cover;
import i5.las2peer.services.ocd.utils.ExecutionStatus;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;

/**
 * A log representation for a CentralityMetric execution.
 * @author Tobias
 *
 */
@Entity
@IdClass(CentralityMetricLogId.class)
public class CentralityMetricLog {

	/*
	 * Database column name definitions.
	 */
	private static final String idColumnName = "ID";
	private static final String typeColumnName = "TYPE";
	private static final String valueColumnName = "VALUE";
	public static final String mapIdColumnName = "COVER_ID";
	public static final String graphIdColumnName = "GRAPH_ID";
	public static final String graphUserColumnName = "USER_NAME";
	private static final String statusIdColumnName = "STATUS";
	
	/*
	 * Field names
	 */
	public static final String STATUS_ID_FIELD_NAME = "statusId";
	
	/**
	 * System generated persistence id.
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = idColumnName)
	private long id;
	/**
	 * The CentralityMap the metric was run on.
	 */
	@Id
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name=graphIdColumnName, referencedColumnName=Cover.graphIdColumnName),
		@JoinColumn(name=graphUserColumnName, referencedColumnName=Cover.graphUserColumnName),
		@JoinColumn(name=mapIdColumnName, referencedColumnName=Cover.idColumnName)
	})
	private CentralityMap map;
	/**
	 * Parameters used by the metric.
	 */
	@ElementCollection
	private Map<String, String> parameters;
	/**
	 * The calculated metric value.
	 */
	@Column(name = valueColumnName)
	private double value;
	/**
	 * Id of the metrics corresponding CentralityMetricType.
	 */
	@Column(name = typeColumnName)
	private int typeId;
	/**
	 * The status of the corresponding execution.
	 */
	@Column(name = statusIdColumnName)
	private int statusId = ExecutionStatus.WAITING.getId();
	
	/**
	 * Creates a new instance.
	 * Only provided for persistence. 
	 */
	protected CentralityMetricLog() {
	}
	
	/**
	 * Creates a new instance.
	 * @param type The type of the corresponding metric.
	 * @param value The value calculated by the metric.
	 * @param parameters The parameters used by the metric.
	 * @param map The CentralityMap the metric was run on.
	 */
	public CentralityMetricLog(CentralityMetricType type, double value, Map<String, String> parameters, CentralityMap map) {
		if(type != null) {
			this.typeId = type.getId();
		}
		else {
			this.typeId = CentralityMetricType.UNDEFINED.getId();
		}
		if(parameters != null) {
			this.parameters = parameters;
		}
		else {
			this.parameters = new HashMap<String, String>();
		}
		this.value = value;
		this.map = map;
	}

	/**
	 * Returns the log id.
	 * @return The id.
	 */
	public long getId() {
		return id;
	}

	/**
	 * Returns the type of the corresponding metric.
	 * @return The type.
	 */
	public CentralityMetricType getType() {
		return CentralityMetricType.lookupType(this.typeId);
	}

	/**
	 * Returns the parameters used by the corresponding metric.
	 * @return A mapping from each parameter name to the corresponding value in String format.
	 */
	public Map<String, String> getParameters() {
		return parameters;
	}

	/**
	 * Returns the value calculated by the corresponding metric.
	 * @return The value.
	 */
	public double getValue() {
		return value;
	}
	
	/**
	 * Sets the corresponding metric value.
	 * @param value The value.
	 */
	public void setValue(double value) {
		this.value = value;
	}
	
	/**
	 * Returns the execution status of the corresponding metric.
	 * @return The status.
	 */
	public ExecutionStatus getStatus() {
		return ExecutionStatus.lookupStatus(statusId);
	}
	
	/**
	 * Sets the execution status of the corresponding metric.
	 * @param status The status.
	 */
	public void setStatus(ExecutionStatus status) {
		this.statusId = status.getId();
	}
	
	/**
	 * Returns the CentralityMap the corresponding metric was run on.
	 * @return The CentralityMap.
	 */
	public CentralityMap getCentralityMap() {
		return map;
	}
	
}
