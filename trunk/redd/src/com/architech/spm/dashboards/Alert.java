package com.architech.spm.dashboards;

/**
 * A transfer object that represents an alert that might be displayed on a geographic region on a map.
 * @author nate
 *
 */
public class Alert {
	
	/**
	 * Enumeration for the <code>type</code> field. Represents an alert that is written as a note.
	 */
	public static final String NOTE = "note";

	/**
	 * The unique ID for this alert.
	 */
	public int id;
	
	/**
	 * geo Specified in the form [Type]/[ID] (e.g. "Territory/T210001")
	 */
	public String geo;
	
	/**
	 * One of the static types on Alert (e.g. Alert.NOTE)
	 */
	public String type;
	
	/**
	 * The main alert message.
	 */
	public String content;
}
