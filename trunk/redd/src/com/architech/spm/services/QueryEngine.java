package com.architech.spm.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.security.RolesAllowed;
import javax.jws.WebMethod;
import javax.jws.WebService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.architech.spm.app.App;
import com.architech.spm.query.MetricDefinition;
import com.architech.spm.query.ResponseGeoMap;
import com.architech.spm.query.ResponseMetricArrayList;
import com.architech.spm.query.ResponseMetricMap;
import com.architech.spm.query.SPMQuery;
import com.architech.spm.query.SPMQueryResponse;

/**
 * A service class that provides methods related to querying the aggregated metrics data.
 * This service class is exposed using JAX-WS and is named <code>SPM Query Engine</code> in
 * the application server.
 * @author nate
 *
 */
@WebService(name="SPM Query Engine")
@RolesAllowed("user")
public class QueryEngine implements ISpringHostedService {
	
	private Log log;
	private HashMap<String, String> levelKey;
	private HashMap<String, String> levelKeyInverted;
	
	public QueryEngine() {
		createMaps();
		createLogger();
	}
	
	// INTERFACE
	
	/**
	 * Runs a single query and returns the results.
	 * @param query The query to exexute
	 * @return The query response
	 */
	public SPMQueryResponse runQuery(SPMQuery query) {
		return getBean().runQueryImpl(query);
	}
	
	/**
	 * Runs multiple queries as a batch, and returns the results. The <code>sequence ID</code> on
	 * <code>SPMQuery</code> and <code>SPMQueryResponse</code> is important to track when using
	 * this method.
	 * @param queries An array of <code>SPMQuery</code> transfer objects
	 * @return An array of <code>SPMQueryResponse</code> transfer objects
	 * @see com.architech.spm.query.SPMQueryResponse#sequence
	 */
	public ArrayList<SPMQueryResponse> runQueryBatch(ArrayList<SPMQuery> queries) {
		return getBean().runQueryBatchImpl(queries);
	}
	
	/**
	 * Lists the known metrics, including the type and related dimension.
	 * @return A list of metrics
	 */
	public ArrayList<MetricDefinition> listMetrics() {
		return getBean().listMetricsImpl();
	}
	
	public String sayHello(String yourName) {
		return yourName + " says hello!";
	}
	
	
	// IMPLEMENTATION
	
	protected SPMQueryResponse runQueryImpl(SPMQuery query) {
		Pattern geoPat = Pattern.compile("^(Territory|Area|Zone)/([a-zA-Z0-9]+)$");
		Pattern colPat = Pattern.compile("^[a-zA-Z0-9_%]+$");
		
		SPMQueryResponse response = new SPMQueryResponse();
		response.sequence = query.sequence;
		response.data = new ResponseGeoMap();
		//int respcount = query.geographies.length * query.products.length;
		
		// generate the "where clause" for the SQL statement, based on the requested geographic areas
		// geo formatted like "Territory/52111002" or "Area/5211" or "Zone/521"
		StringBuilder where = new StringBuilder();
		ArrayList<String> geos = new ArrayList<String>();
		ArrayList<String> levels = new ArrayList<String>();
		for (String geo : query.geographies) {
			Matcher geoMat = geoPat.matcher(geo);
			if (geoMat.matches()) {
				levels.add( levelKey.get(geoMat.group(1)) );
				geos.add(geoMat.group(2));
				if (where.length() == 0) where.append(" WHERE ");
				else where.append(" OR ");
				String level = geoMat.group(1);
				where.append("(\"" + level + "\" = ? AND \"LevelID\" = ?)");
			}
		}
		if (where.length() == 0) {
			log.warn("Forcing no results due to lack of geographies");
			where.append("WHERE 1 = 0"); // don't return anything, missing geos!
		}
		
		// generate the "select" portion of the SQL statement
		StringBuilder select = new StringBuilder("SELECT \"LevelID\", \"Zone\", \"Area\", \"Territory\"");
		
		for (String key : query.columns) {
			Matcher colMat = colPat.matcher(key);
			if (colMat.matches()) {
				if (select.length() > 0) select.append(", ");
				select.append("\"" + key + "\"");
			}
//			ArrayList<Object> value = (key.indexOf("Name")>=0) ? getResponseNames(respcount)
//				: getResponseNumbers(respcount);
//			response.data.put(key, value);
		}
		select.append(" FROM \"ReportData\" ");
		
		// Connect to the database and run the generated query
		Connection conn = null;
		try {
			select.append(where);
			conn = App.getDataSourceConnection();
			PreparedStatement stmt = conn.prepareStatement(select.toString());
				
			log.info(select.toString());
			
			// apply the geographies to the parameters for the prepared statement's "where" clause
			for (int idx=0; idx<geos.size(); idx++) {
				stmt.setString(idx*2+1, geos.get(idx));
				stmt.setString(idx*2+2, levels.get(idx));
				//log.info("parameter " + idx + ": " + geo);
			}
			
			// run the query and pull each result row into the response
			ResultSet result = stmt.executeQuery();
			ResultSetMetaData meta = result.getMetaData();
			while (!result.isAfterLast()) {
				if (result.next()) {
					addResponseRow(response, result, meta);
				}
			}
			
		} catch (SQLException e) {
			log.error("Unable to process query:", e);
		} finally {
			try {
				if (conn != null && !conn.isClosed()) conn.close();
			} catch (SQLException e) {}
		}
		
		return response;
	}
	
	protected ArrayList<SPMQueryResponse> runQueryBatchImpl(ArrayList<SPMQuery> queries) {
		ArrayList<SPMQueryResponse> response = new ArrayList<SPMQueryResponse>();
		for (SPMQuery query : queries) {
			response.add(runQuery(query));
		}
		return response;
	}
	
	protected ArrayList<MetricDefinition> listMetricsImpl() {
		ArrayList<MetricDefinition> metrics = new ArrayList<MetricDefinition>();
		Connection conn = null;
		try {
			conn = App.getDataSourceConnection();
			PreparedStatement stmt = conn.prepareStatement("select M.*, T.TypeKey, D.DimensionKey, M2.DisplayName as \"ParentDisplayName\" " +
					"from SPMMetrics M " +
					"inner join SPMMetricTypes T on M.TypeID = T.TypeID " +
					"inner join SPMMetricDimensions D on M.DimensionID = D.DimensionID " +
					"left join SPMMetrics M2 on M.ParentMetricID = M2.MetricID");
			ResultSet results = stmt.executeQuery();
			while (!results.isAfterLast()) {
				if (results.next()) {
					metrics.add(buildMetric(results));
				}
			}
		} catch (SQLException e) {
			log.error("Unable to process query:", e);
		} finally {
			try {
				if (conn != null && !conn.isClosed()) conn.close();
			} catch (SQLException e) {}
		}
		return metrics;
	}
	
	
	// UTILITIES
	
	private QueryEngine getBean() {
		return (QueryEngine)App.getSpringContext().getBean("QueryEngine");
	}
	
	private void addResponseRow(SPMQueryResponse response, ResultSet result, ResultSetMetaData meta) throws SQLException {
		ResponseMetricMap rec = new ResponseMetricMap();
		
		for (int i=1; i <= meta.getColumnCount(); i++) {
			// get or set up the column's ArrayList
			ResponseMetricArrayList field = rec.metrics.get(meta.getColumnLabel(i));
			if (field == null) {
				field = new ResponseMetricArrayList();
				rec.metrics.put(meta.getColumnLabel(i), field);
			}
			// get the column value
			field.add(result.getObject(i));
		}
		
		// determine geo name for use as the key
		String geoType = levelKeyInverted.get( result.getString("LevelID") );
		String key = geoType + "/" + result.getString(geoType);
		
		response.data.geographies.put(key, rec);
	}
	
	private MetricDefinition buildMetric(ResultSet results) throws SQLException {
		MetricDefinition metric = new MetricDefinition();
		metric.id = results.getInt("MetricID");
		metric.typeId = results.getInt("TypeID");
		metric.dimensionId = results.getInt("DimensionID");
		metric.parentMetricId = results.getInt("ParentMetricID");
		metric.parentMetricName = results.getString("ParentMetricName");
		metric.column = results.getString("Column");
		metric.displayName = results.getString("DisplayName");
		metric.description = results.getString("Description");
		metric.typeKey = results.getString("TypeKey");
		metric.dimensionKey = results.getString("DimensionKey");
		return metric;
	}
	
	@SuppressWarnings("unchecked")
	private void createMaps() {
		levelKey = new HashMap<String, String>();
		levelKey.put("National", "00");
		levelKey.put("Zone", "01");
		levelKey.put("Area", "02");
		levelKey.put("Territory", "03");
		levelKeyInverted = invertMap(levelKey);
	}
	
	private void createLogger() {
		log = LogFactory.getLog(this.getClass());
	}
	
	@SuppressWarnings("unchecked")
	private HashMap invertMap(HashMap source) {
		HashMap dest = new HashMap();
		for (Object key : source.keySet()) {
			dest.put(source.get(key), key);
		}
		return dest;
	}
	
	
	// FAKE DATA
	
//	private ArrayList<Object> getResponseNames(int count) {
//		ArrayList<Object> response = new ArrayList<Object>();
//		for (int i=0; i<count; i++) {
//			response.add( words[ (int)Math.floor(Math.random()*words.length) ] );
//		}
//		return response;
//	}
//	private String[] words = new String[]{"Accumsan","Adipiscing","Aenean","Aliquam","Aliquet","Amet","Ante","Aptent","Arcu","Auctor","Augue","Bibendum","Class","Commodo","Congue","Consectetur","Consequat","Conubia","Convallis","Cras","Cubilia","Curabitur","Curae","Cursus","Dapibus","Diam","Dictum","Dignissim","Dolor","Donec","Dui","Duis","Egestas","Eget","Eleifend","Elit","Enim","Erat","Eros","Est","Euismod","Facilisis","Fames","Faucibus","Felis","Fermentum","Feugiat","Fringilla","Fusce","Gravida","Habitant","Hendrerit","Himenaeos","Iaculis","Imperdiet","Inceptos","Interdum","Ipsum","Justo","Lacus","Laoreet","Lectus","Leo","Libero","Ligula","Litora","Lobortis","Lorem","Luctus","Maecenas","Magna","Malesuada","Massa","Mattis","Mauris","Metus","Mollis","Morbi","Nam","Nec","Neque","Netus","Nibh","Nisi","Nisl","Non","Nostra","Nulla","Nullam","Nunc","Odio","Orci","Ornare","Pellentesque","Per","Pharetra","Phasellus","Placerat","Porta","Porttitor","Posuere","Potenti","Praesent","Pretium","Primis","Proin","Pulvinar","Purus","Quam","Quis","Risus","Rutrum","Sagittis","Sapien","Scelerisque","Sed","Sem","Semper","Senectus","Sit","Sociosqu","Sodales","Sollicitudin","Suscipit","Suspendisse","Taciti","Tellus","Tempor","Tincidunt","Torquent","Tortor","Tristique","Turpis","Ullamcorper","Ultrices","Ultricies","Urna","Varius","Vehicula","Vel","Velit","Venenatis","Vestibulum","Vitae","Vivamus","Viverra","Vulputate"};
//	
//	private ArrayList<Object> getResponseNumbers(int count) {
//		ArrayList<Object> response = new ArrayList<Object>();
//		for (int i=0; i<count; i++) {
//			response.add(Double.valueOf(Math.floor(Math.random()*1000000)));
//		}
//		return response;
//	}
	
}