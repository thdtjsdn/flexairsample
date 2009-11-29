package com.architech.spm.query;

/**
 * A transfer object that represents a metric.
 * @author nate
 *
 */
public class MetricDefinition {
	
	/**
	 * The unique ID for this metric.
	 */
	public int id;
	
	/**
	 * The type ID for this metric.
	 */
	public int typeId;
	
	/**
	 * The type key for this metric.
	 */
	public String typeKey;
	
	/**
	 * The dimension ID for this metric.
	 */
	public int dimensionId;
	
	/**
	 * The dimension key for this metric.
	 */
	public String dimensionKey;
	
	/**
	 * The column name used in the database.
	 */
	public String column;
	
	/**
	 * If this metric is a derived metric (e.g. each "rank" metric is derived from a source metric),
	 * this field refers to the parent metric.
	 */
	public int parentMetricId;
	
	/**
	 * If this metric is a derived metric, this field includes the display name of the parent metric.
	 * @see MetricDefinition#parentMetricId
	 */
	public String parentMetricName;
	
	/**
	 * The display name for this metric.
	 */
	public String displayName;
	
	/**
	 * A description of what this metric means, and/or how it is calculated.
	 */
	public String description;
	
}
