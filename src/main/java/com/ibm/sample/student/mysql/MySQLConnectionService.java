package com.ibm.sample.student.mysql;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class MySQLConnectionService {
	
	protected Connection getConnection() {

		try {
			
			JsonObject credentials = getCredentials();
			
			String jdbcURL = credentials.get("jdbcUrl").getAsString();
			
			Class.forName("com.mysql.jdbc.Driver");
			return DriverManager.getConnection(jdbcURL);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}    
		return null;
	}
	
	protected JsonObject getCredentials() {
	    //for local deployment
	    if(System.getenv("VCAP_SERVICES") == null || System.getenv("VCAP_SERVICES").isEmpty()) {
	    	return readPropertiesFile();
	    }

	    //for bluemix deployment
	    else {
			JsonParser parser = new JsonParser();
		    JsonObject allServices = parser.parse(System.getenv("VCAP_SERVICES")).getAsJsonObject();
			return ((JsonObject)allServices.getAsJsonArray("cleardb").get(0)).getAsJsonObject("credentials");
	    }
	
	}
	
	/**
	 * For local deployment. To retrieve mysql hostname, dbname, username and password from credential.properties
	 * @return credential JsonObject
	 */
	private JsonObject readPropertiesFile() {
		Properties prop = new Properties();
		InputStream input = null;
		JsonObject credentialsJson = new JsonObject();
		try {
			//read from current directory
			input = new FileInputStream("credential.properties");
			// load a properties file
			prop.load(input);

			// get the username and pasword from properties file
			
			String hostname = prop.getProperty("hostname");
			String dbname = prop.getProperty("name");
			String username = prop.getProperty("username");
			String password = prop.getProperty("password");
			
			String jdbcUrl = "jdbc:mysql://" + hostname + "/" + dbname + "?user=" + username + "&password=" + password;
			
			credentialsJson.addProperty("jdbcUrl", jdbcUrl);

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return credentialsJson;
	}
}
