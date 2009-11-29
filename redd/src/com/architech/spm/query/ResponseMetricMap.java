package com.architech.spm.query;

import java.util.HashMap;

/**
 * A transfer object that contains metric values, keyed by the metric name.
 * @author nate
 * @see ResponseGeoMap
 * @see ResponseMetricArrayList
 */
public class ResponseMetricMap {
	
	/**
	 * A <code>HashMap</code> with metric names as keys that contains a series of metric values.
	 */
	public HashMap<String, ResponseMetricArrayList> metrics = new HashMap<String, ResponseMetricArrayList>();
	
}