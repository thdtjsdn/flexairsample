package com.architech.spm.dashboards;

/**
 * A transfer object that represents a dashboard's structure.
 * @author nate
 *
 */
public class DashboardDefinition {
	
	/**
	 * The unique ID for this dashboard.
	 */
	public int id;
	
	/**
	 * The user to which this dashboard has been assigned.  If this field is populated, the dashboard
	 * has been customized for a particular user, overriding another role-specific dashboard.
	 */
	public int userId;
	
	/**
	 * The role to which this dashboard has been assigned.
	 */
	public int roleId;
	
	/**
	 * If this dashboard is a windowed dashboard (as opposed to the main dashboard), this field
	 * will be populated with a value.
	 */
	public int windowId;
	
	/**
	 * A unique key identifying this dashboard. This is used when linking between dashboards.
	 */
	public String key;
	
	/**
	 * The XML definition of the dashboard. This definition includes the hierarchy of display
	 * elements, specific components used, settings for each component, and references to
	 * other dashboards (i.e. windows).
	 */
	public String definition; // XML
}
