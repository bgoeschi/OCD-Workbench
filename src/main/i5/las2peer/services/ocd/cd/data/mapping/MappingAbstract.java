package i5.las2peer.services.ocd.cd.data.mapping;

import javax.persistence.Basic;
import javax.persistence.Embedded;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import i5.las2peer.services.ocd.cd.data.table.Table;
import i5.las2peer.services.ocd.cd.data.table.TableInterface;
import i5.las2peer.services.ocd.cd.data.table.TableLineInterface;
import i5.las2peer.services.ocd.cd.data.table.TableRow;
import i5.las2peer.services.ocd.graphs.properties.GraphProperty;

@MappedSuperclass
public abstract class MappingAbstract implements TableInterface, TableLineInterface{
	
	/**
	 * The id is used as persistence primary key
	 */
	@Id
	@GeneratedValue
	private long id;
	
	/**
	 * The name of the mapping
	 */
	@Basic
	String name;

	/**
	 * Correlation between cooperativity and size
	 */
	@Embedded
	Correlation sizeCorrelation;
	
	/**
	 * Correlation between cooperativity and density
	 */
	@Embedded
	Correlation densityCorrelation;
	
	/**
	 * Correlation between cooperativity and average degree
	 */
	@Embedded
	Correlation averageDegreeCorrelation;
	
	/**
	 * Correlation between cooperativity and degree deviation
	 */
	@Embedded
	Correlation degreeDeviationCorrelation;
	
	/**
	 * Correlation between cooperativity and clustering coefficient
	 */
	@Embedded
	Correlation clusteringCoefficientCorrelation;
	
	private double[] cooperationValues;

	private double[] payoffValues;

	///// Getter /////
	
	/**
	 * Returns a unique id.
	 * 
	 * @return the persistence id
	 */
	@JsonIgnore
	public long getId() {
		return this.id;
	}
	
	/**
	 * Returns the name of this mapping or the id if the mapping have no name
	 * 
	 * @return the name
	 */
	@Override
	@JsonGetter
	public String getName() {
		if(name.isEmpty())
			return String.valueOf(getId());
		return this.name;
	}

	@JsonProperty
	public Correlation getSizeCorrelation() {
		return sizeCorrelation;
	}

	@JsonProperty
	public Correlation getDensityCorrelation() {
		return densityCorrelation;
	}

	@JsonProperty
	public Correlation getAverageDegreeCorrelation() {
		return averageDegreeCorrelation;
	}

	@JsonProperty
	public Correlation getDegreeDeviationCorrelation() {
		return degreeDeviationCorrelation;
	}

	@JsonProperty
	public Correlation getClusteringCoefficientCorrelation() {
		return clusteringCoefficientCorrelation;
	}
	
	/**
	 * Returns the property values in order to compute the correlations
	 * 
	 * @param property The property
	 * @return property values array
	 */
	public abstract double[] getPropertyValues(GraphProperty property);
	
	/**
	 * Returns the cooperation values in order to compute the correlations
	 *
	 * @return cooperation values array
	 */
	public abstract double[] getCooperationValues();

	///// Setter /////

	@JsonSetter
	public void setName(String name) {
		this.name = name;
	}

	@JsonSetter
	public void setSizeCorrelation(Correlation sizeCorrelation) {
		this.sizeCorrelation = sizeCorrelation;
	}

	@JsonSetter
	public void setDensityCorrelation(Correlation densityCorrelation) {
		this.densityCorrelation = densityCorrelation;
	}

	@JsonSetter
	public void setAverageDegreeCorrelation(Correlation averageDegreeCorrelation) {
		this.averageDegreeCorrelation = averageDegreeCorrelation;
	}

	@JsonSetter
	public void setDegreeDeviationCorrelation(Correlation degreeDeviationCorrelation) {
		this.degreeDeviationCorrelation = degreeDeviationCorrelation;
	}
	
	@JsonSetter
	public void setClusteringCoefficient(Correlation clusteringCoefficientCorrelation) {
		this.clusteringCoefficientCorrelation = clusteringCoefficientCorrelation;
	}

	@JsonSetter
	public void setCooperationValues(double[] values) {
		this.cooperationValues = values;
	}

	@JsonSetter
	public void setPayoffValues(double[] values) {
		this.payoffValues = values;
	}

	///// Methods /////

	/**
	 * Create the correlations between the cooperativity values and the property values.
	 */
	public void correlate() {

		double[] cooperationValues = null;
		try {
			cooperationValues = getCooperationValues();

			if (cooperationValues == null || cooperationValues.length < 1)
				return;

			sizeCorrelation = new Correlation(cooperationValues, getPropertyValues(GraphProperty.SIZE));

			densityCorrelation = new Correlation(cooperationValues, getPropertyValues(GraphProperty.DENSITY));

			averageDegreeCorrelation = new Correlation(cooperationValues,
					getPropertyValues(GraphProperty.AVERAGE_DEGREE));

			degreeDeviationCorrelation = new Correlation(cooperationValues,
					getPropertyValues(GraphProperty.DEGREE_DEVIATION));

			clusteringCoefficientCorrelation = new Correlation(cooperationValues,
					getPropertyValues(GraphProperty.CLUSTERING_COEFFICIENT));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public TableRow toTableLine() {

		TableRow line = new TableRow();
		line.add(getSizeCorrelation().toTableLine()).add(getDensityCorrelation().toTableLine())
				.add(getAverageDegreeCorrelation().toTableLine()).add(getDegreeDeviationCorrelation().toTableLine()).add(getClusteringCoefficientCorrelation().toTableLine());
		return line;
	}
		
	@Override
	public Table toTable() {
		return new Table();
	}

}
