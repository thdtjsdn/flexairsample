package com.architech.spm.query;

/**
 * A transfer object that represents the response from a query run with the <code>QueryEngine</code>.
 * @author nate
 * @see com.architech.spm.services.QueryEngine
 * @see SPMQuery
 */
public class SPMQueryResponse {
	
	/**
	 * Sequence number in a batch. This sequence number will match a sequence number
	 * from the SPMQuery objects sent to {@link com.architech.spm.services.QueryEngine#runQueryBatch(java.util.ArrayList)}.
	 */
	public int sequence;
	
	/**
	 * Response data, consisting of a hierarchy of objects from three unique transfer classes.
	 * @see ResponseGeoMap
	 * @see ResponseMetricMap
	 * @see ResponseMetricArrayList
	 */
	public ResponseGeoMap data;
	
}