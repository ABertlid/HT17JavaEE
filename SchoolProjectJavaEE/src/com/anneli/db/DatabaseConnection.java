package com.anneli.db;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DatabaseConnection {

	private static DatabaseConnection initialize;
	protected DataSource dataSource;
	private String jndiName = "java:comp/env/jdbc/hibernate_serie";

	protected DatabaseConnection() throws Exception {
		dataSource = getDataSource();
	}

	public static DatabaseConnection getInitialize() throws Exception {
		if (initialize == null) {
			initialize = new DatabaseConnection();
		}
		return initialize;
	}

	private DataSource getDataSource() throws NamingException {
		Context context = new InitialContext();
		DataSource theDataSource = (DataSource) context.lookup(jndiName);

		return theDataSource;
	}

}
