package com.hummingbee.garden.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBHelper {
	private static String dbUser;
	private static String dbPasswd;
	private static String dbPort;
	private static String DB_NAME = "garden_system";
	
	public static void setDbUser(String user){
		dbUser = user;
	}
	
	public static void setDbPasswd(String passwd){
		dbPasswd = passwd;
	}
	
	public static void setDbPort(String port){
		dbPort = port;
	}
	
	static{
		try{
	        Class.forName("com.mysql.jdbc.Driver");
	    }catch(ClassNotFoundException e){
	        e.printStackTrace();
	    }
	}
	
	public static boolean initDatabase(){
		Connection dbConn = null;
		String dbUrl = String.format("jdbc:MySQL://127.0.0.1:%s", dbPort);
		try {
			dbConn = DriverManager.getConnection(dbUrl, dbUser, dbPasswd);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		Statement statement = null;
		try{
			statement = dbConn.createStatement();
			statement.execute(String.format("CREATE DATABASE IF NOT EXISTS %s", DB_NAME));
			statement.execute(String.format("USE %s", DB_NAME));
			statement.execute("CREATE TABLE IF NOT EXISTS sprinkler (id int(11) unsigned NOT NULL AUTO_INCREMENT, sprinkler_group varchar(16) NOT NULL DEFAULT '',"
					+ "sprinkler_id varchar(16) NOT NULL DEFAULT '', location varchar(16) NOT NULL DEFAULT '', is_on tinyint(1) NOT NULL DEFAULT '0',"
					+ "is_functional tinyint(1) NOT NULL DEFAULT '1', water_flux varchar(12) NOT NULL DEFAULT '50', PRIMARY KEY (id))");
			statement.execute("CREATE TABLE IF NOT EXISTS water_usage (id int(11) unsigned NOT NULL AUTO_INCREMENT, sprinkler_group varchar(16) NOT NULL DEFAULT '',"
					+ "sprinkler_id varchar(4) NOT NULL DEFAULT '', date varchar(16) NOT NULL DEFAULT '', volume varchar(16) NOT NULL DEFAULT '', PRIMARY KEY (id))");
			statement.execute("CREATE TABLE IF NOT EXISTS weekly_plan (id int(11) unsigned NOT NULL AUTO_INCREMENT, sprinkler_group varchar(16) NOT NULL DEFAULT '',"
					+ "monday varchar(16) DEFAULT '', tuesday varchar(16) DEFAULT '', wednesday varchar(16) DEFAULT '', thursday varchar(16) DEFAULT '',"
					+ "friday varchar(16) DEFAULT '', saturday varchar(16) DEFAULT '', sunday varchar(16) DEFAULT '', PRIMARY KEY (id))");
			return true;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return false;
	}
	
	public static Connection connectDatabase(){
		Connection dbConn = null;
		String dbUrl = String.format("jdbc:MySQL://127.0.0.1:%s/%s", dbPort, DB_NAME);
		try {
			dbConn = DriverManager.getConnection(dbUrl, dbUser, dbPasswd);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return dbConn;
	}
	
	public static void closeDbConnection(Connection dbConnection){
		if(dbConnection == null)
			return;
		try {
			dbConnection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public static ResultSet queryDatabase(Connection dbConn, String query){
		ResultSet resultSet = null;
		Statement statement = null;
		try{
			statement = dbConn.createStatement();
			resultSet = statement.executeQuery(query);
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return resultSet;
	}
	
	public static void executeSql(Connection dbConn, String sqlStr){
		try{
			Statement statement = dbConn.createStatement();
			statement.execute(sqlStr);
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public static int updateDatabase(Connection dbConn, String query){
		int updateCount = 0;
		Statement statement = null;
		try{
			statement = dbConn.createStatement();
			updateCount = statement.executeUpdate(query);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return updateCount;
	}
}
