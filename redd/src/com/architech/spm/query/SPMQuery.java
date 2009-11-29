package com.architech.spm.query;

/**
 * A transfer object that represents a query for the <code>QueryEngine</code> to execute.
 * @author nate
 * @see com.architech.spm.services.QueryEngine
 * @see SPMQueryResponse
 */
public class SPMQuery {
	
	/**
	 * sequence number if used in a batch
	 */
	public int sequence;
	
	/**
	 * data source
	 */
	public String dataset;
	
	/**
	 * which metrics or fields to retrieve
	 */
	public String[] columns;
	
	/**
	 * geo locations for a filter
	 */
	public String[] geographies;
	
	/**
	 * products for a filter
	 */
	public String[] products;
	
}