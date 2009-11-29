package com.architech.spm.services;

import java.util.ArrayList;

import javax.annotation.security.RolesAllowed;
import javax.jws.WebService;

import com.architech.spm.dashboards.Alert;
import com.architech.spm.dashboards.DashboardDefinition;
import com.architech.spm.dashboards.SupportArticle;

/**
 * A service class that provides methods related to dashboards, including
 * the dashboard definitions, alerts, and support articles. This service class is exposed using
 * JAX-WS and is named <code>SPM Dashboards</code> in the application server.
 * @author nate
 *
 */
@WebService(name="SPM Dashboards")
@RolesAllowed("user")
public class Dashboards {
	
	
	///////////////////////////////
	//        DASHBOARDS         //
	///////////////////////////////
	
	
	/**
	 * Returns a list of all dashboards (primary plus windows) defined for the current user's role.
	 * If a dashboard has been created for this specific user, it will replace the role-specific
	 * dashboard, and the role-specific dashboard will not be returned.
	 * @return A list of user-specific dashboards
	 */
	public ArrayList<DashboardDefinition> listDashboards() {
		ArrayList<DashboardDefinition> dashes = new ArrayList<DashboardDefinition>();
		return dashes;
	}
	
	/**
	 * Get a specific dashboard by ID.
	 * @param id
	 * @return The requested dashboard definition.
	 */
	public DashboardDefinition getDashboard(int id) {
		DashboardDefinition dash = new DashboardDefinition();
		dash.id = id;
		return dash;
	}
	
	/**
	 * Create a new dashboard or save changes to an existing dashboard.
	 * @param dash
	 * @return The ID of the new (or existing) dashboard.
	 */
	public int saveDashboard(DashboardDefinition dash) {
		return dash.id;
	}
	
	/**
	 * Delete an existing dashboard.
	 * @param id
	 * @return True, if deletion was successful, otherwise false.
	 */
	public boolean deleteDashboard(int id) {
		return true;
	}
	
	
	///////////////////////////////
	//           ALERTS          //
	///////////////////////////////
	
	/**
	 * Looks up a set of geographic areas by key.
	 * @param geoKeys One or more geographic keys, specified in the form [Type]/[ID] (e.g. "Territory/T210001")
	 * @return A list of alerts that are associated with any of the supplied geographic areas.
	 */
	public ArrayList<Alert> listAlerts(String[] geoKeys) {
		ArrayList<Alert> alerts = new ArrayList<Alert>();
		Alert one = new Alert();
		one.geo = (geoKeys != null && geoKeys.length > 0) ? geoKeys[0] : "";
		one.type = Alert.NOTE;
		one.content = "Lorem ipsum dolor sit amet";
		alerts.add(one);
		return alerts;
	}
	
	/**
	 * Create a new alert or save changes to an existing alert.
	 * @param alert
	 * @return The ID of the new (or existing) alert.
	 */
	public int saveAlert(Alert alert) {
		return alert.id;
	}
	
	/**
	 * Delete an existing alert.
	 * @param id
	 * @return True, if deletion was successful, otherwise false.
	 */
	public boolean deleteAlert(int id) {
		return true;
	}
	
	
	///////////////////////////////
	//     DOCS AND SUPPORT      //
	///////////////////////////////
	
	/**
	 * Returns all articles for the requested area of the application.
	 * @param appArea The application area.
	 * @return A list of support articles.
	 */
	public ArrayList<SupportArticle> listSupportArticles(String appArea) {
		ArrayList<SupportArticle> articles = new ArrayList<SupportArticle>();
		return articles;
	}
	
	/**
	 * Create a new support article or save changes to an existing support article.
	 * @param article The new article to save
	 * @return The ID of the new (or existing) support article.
	 */
	public int saveSupportArticle(SupportArticle article) {
		return article.id;
	}

	/**
	 * Delete an existing support article.
	 * @param id
	 * @return True, if deletion was successful, otherwise false.
	 */
	public boolean deleteSupportArticle(int id) {
		return true;
	}
	
}
