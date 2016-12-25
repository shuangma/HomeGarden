package com.hummingbee.garden.model.sprinkler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;

import com.hummingbee.garden.common.DBHelper;
import com.hummingbee.garden.common.Utils;

public class WeeklyPlan {
	private Connection dbConnection = null;
	private ArrayList<String> groups = new ArrayList<String>();
	
	private HashMap<String, ArrayList<HashMap<String, String>>> groupWeeklyPlanMap = new HashMap<String, ArrayList<HashMap<String, String>>>();
	
	
	public WeeklyPlan(ArrayList<String> groups) {
		for(String group : groups)
			this.groups.add(group);
		
		initiateWeeklyPlan(groups);
	}
	
	public void closeDbConnection(){
		DBHelper.closeDbConnection(dbConnection);
	}
	
	/**
	 * Initiate the weekly plan, try to load the plan from the database,
	 * if loading failed, then initialize an empty weekly plan
	 * @param groups An ArrayList of sprinkler groups
	 */
	private void initiateWeeklyPlan(ArrayList<String> groups) {
		loadPlanFromDatabase();
	}
	
	private boolean loadPlanFromDatabase(){
		if(!isDbConnected())
			return false;
		String query = String.format("SELECT sprinkler_group, sunday, monday, tuesday, wednesday, thursday, friday, saturday from %s", Utils.WEEKLY_PLAN_TABLE);
		ResultSet resultSet = DBHelper.queryDatabase(dbConnection, query);
		if(resultSet == null)
			return false;
		try{
			while(resultSet.next())
	        {
	           String groupName = resultSet.getString("sprinkler_group");
	           if(!groupWeeklyPlanMap.containsKey(groupName)){
	        	   ArrayList<HashMap<String, String>> weeklyPlanList = new ArrayList<HashMap<String, String>>();
	        	   groupWeeklyPlanMap.put(groupName, weeklyPlanList);
	           }
	           ArrayList<HashMap<String, String>> weeklyPlanList = groupWeeklyPlanMap.get(groupName);
	           
	           HashMap<String, String> dayPlanMap = new HashMap<String, String>();
	           
	           String[] weekDays = new DateFormatSymbols().getWeekdays();
	           for(String weekDay : weekDays){
	        	   if(weekDay == null || weekDay.equals(""))
	        		   continue;
	        	   String dayPlan = resultSet.getString(weekDay.toLowerCase());
	        	   if(dayPlan != null){
	        		   dayPlanMap.put(weekDay.toLowerCase(), dayPlan);
	        	   }else
	        		   dayPlanMap.put(weekDay.toLowerCase(), "");
	           }
	           weeklyPlanList.add(dayPlanMap);
	           
	           groupWeeklyPlanMap.put(groupName, weeklyPlanList);
	        }
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Store the weekly plan into database
	 */
	public void storeWeeklyPlan(){
		if(!isDbConnected())
			return;
		
		this.clearTable();
		
		 String[] weekDays = new DateFormatSymbols().getWeekdays();
		 
		for(String group : groupWeeklyPlanMap.keySet()){
			ArrayList<HashMap<String, String>> dayPlanList = groupWeeklyPlanMap.get(group);
			
			for(HashMap<String, String> dayPlanMap : dayPlanList){
				ArrayList<String> tmpPlanList = new ArrayList<String>();
				for(String day : weekDays){
					if(day == null || day.equals(""))
						continue;
					String dayPlan = dayPlanMap.get(day.toLowerCase());
					tmpPlanList.add(String.format("\"%s\"", dayPlan));
				}
				String joinedPlan = Utils.join(tmpPlanList, ",");
				String update = String.format("INSERT INTO %s (sprinkler_group, sunday, monday, tuesday, wednesday, thursday, friday, saturday) VALUES (\"%s\", %s)", Utils.WEEKLY_PLAN_TABLE, group, joinedPlan);
				DBHelper.updateDatabase(dbConnection, update);
			}
		}
	}
	
	/**
	 * Add a plan for a specified group under a certain day
	 * @param group the group name which the sprinkler belongs to
	 * @param day the day name
	 * @param plan added plan, in the format 8:00-9:30
	 */
	public void addDayPlan(String group, String day, String plan){
		if(!isGroupNameValid(group) || !isDayNameValid(day))
			return;
		
		ArrayList<HashMap<String, String>> dayPlanList = groupWeeklyPlanMap.get(group);
		HashMap<String, String> dayPlanMap = new HashMap<String, String>();
		dayPlanMap.put(day, plan);
		
		dayPlanList.add(dayPlanMap);
		groupWeeklyPlanMap.put(group, dayPlanList);
	}
	
	public void addEmptyWeeklyPlan(String group){
		if(!isGroupNameValid(group))
			return;
		
		ArrayList<HashMap<String, String>> dayPlanList = groupWeeklyPlanMap.get(group);
		HashMap<String, String> dayPlanMap = new HashMap<String, String>();
		for(String day : new DateFormatSymbols().getWeekdays()){
			if(day == null || day.equals(""))
				dayPlanMap.put(day.toLowerCase(), "");
		}
		
		dayPlanList.add(dayPlanMap);
		groupWeeklyPlanMap.put(group, dayPlanList);
	}
	
	/**
	 * Update a day plan for a specified group under a certain day
	 * @param group the group name which the sprinkler belongs to
	 * @param weeklyPlan updated plan
	 */
	public void updateWeeklyPlan(String group, ArrayList<HashMap<String, String>> weeklyPlan){
		if(!isGroupNameValid(group))
			return;
		groupWeeklyPlanMap.put(group, weeklyPlan);
	}
	
	
	private void clearTable(){
		if(!isDbConnected())
			return;
		String query = String.format("TRUNCATE %s", Utils.WEEKLY_PLAN_TABLE);
		DBHelper.executeSql(dbConnection, query);
	}
	

	public ArrayList<HashMap<String, String>> getGroupWeeklyPlan(String group) {
		if(group == null)
			return null;
		return groupWeeklyPlanMap.get(group);
	}
	
	/**
	 * Get the daily plan for a given group under a certain day
	 * @param group the group day of a sprinkler
	 * @param day the given day
	 * @return An ArrayList containing the daily plan
	 */
	public ArrayList<String> getGroupDayPlan(String group, String day) {
		if(group == null || day == null)
			return null;
		ArrayList<HashMap<String, String>> groupWeeklyPlan = getGroupWeeklyPlan(group);
		if(groupWeeklyPlan == null)
			return null;
		ArrayList<String> dayPlanList = new ArrayList<String>();
		for(HashMap<String, String> dayPlanMap : groupWeeklyPlan){
			String dayPlan = dayPlanMap.get(day);
			if(dayPlan == null || dayPlan.equals(""))
				continue;
			dayPlanList.add(dayPlan);
		}
		return dayPlanList;
	}
	
	private boolean isDbConnected(){
		if(dbConnection == null)
			dbConnection = DBHelper.connectDatabase();
		if(dbConnection == null)
			return false;
		else
			return true;
	}
	
	private boolean isGroupNameValid(String group){
		if(group == null)
			return false;
		return groups.contains(group);
	}
	
	private boolean isDayNameValid(String day){
		if(day == null)
			return false;
		String[] weekDays = new DateFormatSymbols().getWeekdays();
		for(String weekDay : weekDays){
			if(weekDay.equals(day))
				return true;
		}
		return false;
	}
	
	public ArrayList<String> getSprinklerGroups(){
		return groups;
	}
}

