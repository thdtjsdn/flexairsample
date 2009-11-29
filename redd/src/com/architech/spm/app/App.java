package com.architech.spm.app;

import java.sql.Connection;
import java.sql.SQLException;

//import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import org.apache.commons.logging.Log;

/**
 * This static class is responsible for initializing the Spring context and providing easy access
 * to the DataSource for the SPM database.
 * @author nate
 *
 */
public class App {
	private static ApplicationContext springContext;
	private static DataSource pool;
	private static Log log;
	
	static {
		log = LogFactory.getLog(App.class);
		springContext = new ClassPathXmlApplicationContext("spring-beans.xml");
		
//		Context env = null;
		try {
//			env = (Context) new InitialContext().lookup("java:comp/env");
//			pool = (DataSource)env.lookup("jdbc/SpmReckitt");
			pool = (DataSource) new InitialContext().lookup("jdbc/SpmReckitt");
			if (pool == null) {
				log.error("unknown DataSource");
			}
		} catch(NamingException ne) {
			log.error("Error connecting to data source:", ne);
		}
	}
	
	/**
	 * A getter for the application's Spring context.
	 * @return the loaded Spring context
	 */
	public static ApplicationContext getSpringContext() {
		return springContext;
	}
	
	/**
	 * A helper function to create a new <code>Connection</code> from the application's
	 * <code>DataSource</code>.
	 * @return A new <code>Connection</code> object
	 * @throws SQLException
	 */
	public static Connection getDataSourceConnection() throws SQLException {
		if (pool == null) {
			log.error("getDataSourceConnection: No connection pool is available!");
		}
		return pool.getConnection();
	}
	
}
