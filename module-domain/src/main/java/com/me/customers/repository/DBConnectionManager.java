
package com.me.customers.repository;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.log4j.Logger;
import java.sql.Connection;
import java.sql.SQLException;

public class DBConnectionManager{

	private String dbURL;
	private String user;
	private String password;
	private String driver;
	private BasicDataSource dataSource;
	private static final Logger LOG = Logger.getLogger(DBConnectionManager.class);

	public DBConnectionManager(String dbURL, String user, String password, String driver){
		this.dbURL = dbURL;
		this.user = user;
		this.password = password;
		this.driver = driver;
		this.setup();
	}

	private void setup(){
		dataSource = new BasicDataSource();
		dataSource.setUrl(dbURL);
		dataSource.setUsername(user);
		dataSource.setPassword(password);
		dataSource.setMinIdle(50);
		dataSource.setMaxIdle(100);
		dataSource.setMaxOpenPreparedStatements(1000);
		dataSource.setDriverClassName(driver);
	}

	public Connection getConnection(){
		try{
			return dataSource.getConnection();
		}catch(SQLException ex){
			LOG.error(ex.getMessage());
			throw new DataException(ex.getMessage());
		}
	}

	public void closeConnection(){
		try{
			dataSource.close();
		}catch(SQLException ex){
			LOG.error(ex.getMessage());
			throw new DataException(ex.getMessage());
		}
	}

}
