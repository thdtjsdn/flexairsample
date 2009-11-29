package com.architech.spm.query;

import java.util.HashMap;

/**
 * A transfer object that contains sets of metrics, keyed by a geographic area's ID.
 * @author nate
 * @see ResponseMetricMap
 * @see ResponseMetricArrayList
 */
public class ResponseGeoMap {
	
	/**
	 * A <code>HashMap</code> with geographic area IDs as keys that contains metrics.
	 */
	public HashMap<String, ResponseMetricMap> geographies = new HashMap<String, ResponseMetricMap>();
	
}
