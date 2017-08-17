package i5.las2peer.services.ocd.metrics.centrality;

import i5.las2peer.services.ocd.graphs.CentralityMapId;

/**
 * Composite persistence id of a CentralityMetricLog.
 * @author Tobias
 *
 */
public class CentralityMetricLogId {

	/**
	 * The specific log id.
	 */
    private long id;
    
    /**
     * The id of the corresponding cover.
     */
    private CentralityMapId mapId;
 
    /**
     * Creates a new instance.
     * @param id The log id.
     * @param mapId The id of the corresponding CentralityMap.
     */
    public CentralityMetricLogId(long id, CentralityMapId mapId) {
        this.id = id;
        this.mapId = mapId;
    }
 
    @Override
    public boolean equals(Object object) {
        if (object instanceof CentralityMetricLogId) {
        	CentralityMetricLogId pk = (CentralityMetricLogId)object;
            return mapId.equals(mapId) && id == pk.id;
        } else {
            return false;
        }
    }
 
    @Override
    public int hashCode() {
        return (int)(id + mapId.hashCode());
    }
    
    /**
     * Returns the id of the corresponding CentralityMap.
     * @return The id.
     */
    public CentralityMapId getCentralityMapId() {
    	return mapId;
    }
    
    /**
     * Returns the specific log id.
     * @return The id.
     */
    public long getId() {
    	return id;
    }
	
}
