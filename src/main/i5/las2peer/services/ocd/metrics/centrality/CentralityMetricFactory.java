package i5.las2peer.services.ocd.metrics.centrality;

import i5.las2peer.services.ocd.utils.ConditionalParameterizableFactory;

import java.util.Map;

/**
 * A factory for producing centrality metrics using CentralityMetricType objects as descriptors.
 * @author Tobias
 *
 */
public class CentralityMetricFactory implements ConditionalParameterizableFactory<CentralityMetric, CentralityMetricType> {

	@Override
	public CentralityMetric getInstance(CentralityMetricType metricType, Map<String, String> parameters) throws InstantiationException, IllegalAccessException {
		if(isInstantiatable(metricType)) {
			CentralityMetric metric = metricType.getMetricClass().newInstance();
			metric.setParameters(parameters);
			return metric;
		}
		throw new IllegalStateException("This metric is not instantiatable.");
	}
	
	@Override
	public boolean isInstantiatable(CentralityMetricType metricType) {
		if(metricType.getMetricClass().equals(CentralityMetric.class)) {
			return false;
		}
		else {
			return true;
		}
	}

}
