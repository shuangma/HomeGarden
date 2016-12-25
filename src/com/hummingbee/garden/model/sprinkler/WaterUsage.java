package com.hummingbee.garden.model.sprinkler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.HashMap;

import com.hummingbee.garden.common.DBHelper;
import com.hummingbee.garden.common.Utils;

public class WaterUsage {
	private static final DecimalFormat decimalFormat = new DecimalFormat("#.00"); 
	
	private Connection dbConnection = null;
	private String group;
	private String sprinklerId;
	
	private HashMap<String, Double> dailyWaterUsageMap = new HashMap<String, Double>();
	private HashMap<Integer, Double> monthlyWaterUsageMap = new HashMap<Integer, Double>();
	
	public WaterUsage(String group, String sprinklerId){
		this.group = group;
		this.sprinklerId = sprinklerId;
		
		loadWaterUsageFromDatabase(group, sprinklerId);
	}
	
	public void closeDbConnection(){
		DBHelper.closeDbConnection(dbConnection);
	}
	
	public void addWaterUsage(String date, double dailyAddedVolume){
		if(date == null || date.equals(""))
			return;
		dailyAddedVolume = Double.valueOf(decimalFormat.format(dailyAddedVolume));
		
		double dailyVolume = 0.0;
		if(dailyWaterUsageMap.containsKey(date))
			dailyVolume = dailyWaterUsageMap.get(date);
		dailyVolume += dailyAddedVolume;
		dailyWaterUsageMap.put(date, dailyVolume);
		
		int month = Utils.getMonthFromString(date);
		if(month == -1)
			return;
		double monthlyVolume = 0.0;
		if(!monthlyWaterUsageMap.containsKey(month)){
			monthlyWaterUsageMap.put(month, monthlyVolume);
		}
		monthlyVolume = monthlyWaterUsageMap.get(month);
		monthlyVolume += dailyAddedVolume;
		monthlyWaterUsageMap.put(month, monthlyVolume);
	}
	
	public HashMap<Integer, Double> getMonthlyWaterUsage(){
		return monthlyWaterUsageMap;
	}
	
	public HashMap<String, Double> getDailyWaterUsage(){
		return dailyWaterUsageMap;
	}
	
	private void loadWaterUsageFromDatabase(String group, String sprinklerId){
		if(!isDbConnected())
			return;
		String query = String.format("SELECT date, volume FROM %s WHERE sprinkler_group=\"%s\" and sprinkler_id=\"%s\"", 
				Utils.WATER_USAGE_TABLE, group, sprinklerId);
		ResultSet resultSet = DBHelper.queryDatabase(dbConnection, query);
		if(resultSet == null)
			return;
		try{
			while(resultSet.next()){
				String date = resultSet.getString("date");
				String volume = resultSet.getString("volume");
				if(date == null || date.equals("") || volume == null || volume.equals(""))
					continue;
				dailyWaterUsageMap.put(date, Double.valueOf(volume));
				int month = Utils.getMonthFromString(date);
				if(month == -1)
					continue;
				double monthlyVolume = 0.0;
				if(!monthlyWaterUsageMap.containsKey(month)){
					monthlyWaterUsageMap.put(month, monthlyVolume);
				}
				monthlyVolume = monthlyWaterUsageMap.get(month);
				monthlyVolume += Double.valueOf(volume);
				monthlyWaterUsageMap.put(month, monthlyVolume);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	/**
	 * Store water usage detail into database
	 */
	public void storeWaterUsage(){
		if(!isDbConnected())
			return;
		
		for(String date : dailyWaterUsageMap.keySet()){
			double waterUsage = dailyWaterUsageMap.get(date);
			String waterUsageToStore = decimalFormat.format(waterUsage);
			// If the record exists already, use update instead of insert
			if(isRecordExists(date)){
				String update = String.format("UPDATE %s set volume=\"%s\" where sprinkler_group=\"%s\" and sprinkler_id=\"%s\" and date=\"%s\"", 
						Utils.WATER_USAGE_TABLE, waterUsageToStore, this.group, this.sprinklerId, date);
				DBHelper.updateDatabase(dbConnection, update);
			}else{
				String insert = String.format("INSERT INTO %s (sprinkler_group, sprinkler_id, date, volume) VALUES (\"%s\", \"%s\", \"%s\", \"%s\")",
						Utils.WATER_USAGE_TABLE,this.group, this.sprinklerId, date, waterUsageToStore);
				DBHelper.updateDatabase(dbConnection, insert);
			}
		}
	}
	
	private boolean isRecordExists(String date){
		String query = String.format("SELECT COUNT(*) AS total FROM %s WHERE sprinkler_group=\"%s\" and sprinkler_id=\"%s\" and date=\"%s\"", Utils.WATER_USAGE_TABLE,
				this.group, this.sprinklerId, date);
		ResultSet resultSet = DBHelper.queryDatabase(dbConnection, query);
		if(resultSet == null)
			return false;
		try{
			while(resultSet.next()){
				if(resultSet.getInt("total") >= 1){
					return true;
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return false;
	}
	
	private boolean isDbConnected(){
		if(dbConnection == null)
			dbConnection = DBHelper.connectDatabase();
		if(dbConnection == null)
			return false;
		else
			return true;
	}
}
