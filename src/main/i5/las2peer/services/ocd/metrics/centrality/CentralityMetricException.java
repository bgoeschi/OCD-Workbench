package i5.las2peer.services.ocd.metrics.centrality;

import i5.las2peer.services.ocd.utils.AbstractCustomException;

/**
 * An exception that indicates the failure of the execution of a centrality metric.
 * @author Tobias
 *
 */
public class CentralityMetricException extends AbstractCustomException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7785040553039784645L;
	
	/**
	 * A standard message for all benchmark exceptions.
	 */
	private static final String errorMessage = "Metric could not be calculated.";
	
	/**
	 * Creates an exception whose error message includes detailed information
	 * from an additional exception.
	 * @param e The additional exception.
	 */
	public CentralityMetricException(Exception e) {
		super(errorMessage + "\nInternal Exception:\n" + getInternalExceptionString(e));
	}
	
	/**
	 * Creates an exception whose message includes an additional string.
	 * @param s The additional string.
	 */
	public CentralityMetricException(String s) {
		super(errorMessage + "\n" + s);
	}
	
	/**
	 * Creates a standard exception.
	 */
	public CentralityMetricException() {
		super(errorMessage);
	}
	
	/**
	 * Creates an exception whose message includes detailed information from an additional exception
	 * and an additional string.
	 * @param e The additional exception.
	 * @param s The additional string.
	 */
	public CentralityMetricException(Exception e, String s) {
		super(errorMessage + "\n" + s + "\nInternal Exception:\n" + getInternalExceptionString(e));
	}
	
	
}
