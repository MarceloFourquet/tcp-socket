
package com.me.customers.repository;

import com.me.customers.model.Customer;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import org.apache.log4j.Logger;

public class CustomerDAO{

	private static DBConnectionManager dbConnectionManager;
	private static final Logger LOG = Logger.getLogger(CustomerDAO.class);

	public CustomerDAO(){
		initializeDBConnectionManager();
	}

	private void initializeDBConnectionManager(){
		if(dbConnectionManager == null){
			try{
				Properties prop = new Properties();
				prop.load(CustomerDAO.class.getClassLoader().getResourceAsStream("jdbc.properties"));
				dbConnectionManager = new DBConnectionManager(
				prop.getProperty("jdbc.url"),
				prop.getProperty("jdbc.user"),
				prop.getProperty("jdbc.password"),
				prop.getProperty("jdbc.driver"));
			}catch(IOException ex){
				LOG.error(ex.getMessage());
			}
		}
	}

	public void createCustomer(String[] customerData){
		Customer customer = new Customer();
		customer.setName(customerData[0].trim());
		customer.setLast_name(customerData[1].trim());
		customer.setDni(Integer.valueOf(customerData[2]));
		customer.setAddress(customerData[3].trim());
		customer.setEmail(customerData[4]);
		customer.setPhone(customerData[5]);
		try{
			saveCustomer(customer);
		}catch(SQLException ex){
			LOG.error(ex.getMessage());
		}
	}

	public void saveCustomer(Customer customer) throws SQLException{
		final String query = "insert into customer (name, last_name, dni, address, email, phone) values (?, ?, ?, ?, ?, ?)";
		try(PreparedStatement stmt = dbConnectionManager.getConnection().prepareStatement(query)){
			stmt.setString(1, customer.getName());
			stmt.setString(2, customer.getLast_name());
			stmt.setInt(3, customer.getDni());
			stmt.setString(4, customer.getAddress());
			stmt.setString(5, customer.getEmail());
			stmt.setString(6, customer.getPhone());
			stmt.execute();
		}
	}

	public Customer consulta(int id) throws SQLException{
		Customer customer = null;
		ResultSet result = null;
		final String query = "select * from customer where id = ?";
		try(PreparedStatement stmt = dbConnectionManager.getConnection().prepareStatement(query)){
			stmt.setInt(1, id);
			result = stmt.executeQuery();
			if(result != null){
				customer = new Customer();
				while(result.next()){
					customer.setId(result.getInt("id"));
					customer.setName(result.getString("name"));
					customer.setLast_name(result.getString("last_name"));
					customer.setDni(result.getInt("dni"));
					customer.setAddress(result.getString("address"));
					customer.setEmail(result.getString("email"));
					customer.setPhone(result.getString("phone"));
				}
			}
		}finally{
			if(result != null){
				result.close();
			}
		}
		return customer;
	}

}
