package com.hummingbee.garden.model.sprinkler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import com.hummingbee.garden.common.DBHelper;
import com.hummingbee.garden.common.Utils;
import com.hummingbee.garden.model.simulation.DateTimeSimulator;
import com.hummingbee.garden.model.simulation.TemperatureSimulator;

public class SprinklerBank extends Observable{
	public static final String SPRINKLER_BANK_OBSERVABLE = "sprinkler_bank";
	
	private Connection dbConnection = null;
	
	HashMap<String, ArrayList<Sprinkler>> groupSprinklerMap = new HashMap<String, ArrayList<Sprinkler>>();
	HashMap<String, Sprinkler> idSprinklerMap = new HashMap<String, Sprinkler>();
	
	
	public SprinklerBank() {
		loadSprinklersFromDatabase();
	}
	
	// Some event performs
	public void turnOffSprinkler(String sprinklerId, boolean userControl){
		Sprinkler sprinkler = idSprinklerMap.get(sprinklerId);
		if(sprinkler == null)
			return;
		sprinkler.setSprinklerEnabled(false);
		
		sprinkler.turnOffSprinkler(userControl);
		notifyChanges();
	}
	
	public void turnOnSprinkler(String sprinklerId, boolean userControl){
		Sprinkler sprinkler = idSprinklerMap.get(sprinklerId);
		if(sprinkler == null)
			return;
		sprinkler.setSprinklerEnabled(true);
		
		sprinkler.turnOnSprinkler(userControl);
		notifyChanges();
	}
	
	public void turnOffGroupSprinkler(String group, boolean userControl){
		Collection<Sprinkler> sprinklers = groupSprinklerMap.get(group);
		if(sprinklers == null)
			return;
		
		for(Sprinkler sprinkler : sprinklers){
			sprinkler.turnOffSprinkler(userControl);
			sprinkler.setSprinklerEnabled(false);
		}
		notifyChanges();
	}
	
	public void turnOnGroupSprinkler(String group, boolean userControl){
		Collection<Sprinkler> sprinklers = groupSprinklerMap.get(group);
		if(sprinklers == null)
			return;
		
		for(Sprinkler sprinkler : sprinklers){
			sprinkler.turnOnSprinkler(userControl);
			sprinkler.setSprinklerEnabled(true);
		}
		notifyChanges();
	}
	
	public void updateSprinklerWaterFlux(String sprinklerId, double waterFlux){
		if(sprinklerId == null || sprinklerId.equals(""))
			return;
		Sprinkler sprinkler = idSprinklerMap.get(sprinklerId);
		if(sprinkler == null)
			return;
		sprinkler.setWaterFlux(waterFlux);
	}
	
	public void notifyChanges(){
		setChanged();
		notifyObservers(SPRINKLER_BANK_OBSERVABLE);
	}
	
	/**
	 * Load the sprinkler data from the database
	 */
	private void loadSprinklersFromDatabase(){
		if(!isDbConnected()){
			return;
		}
		String query = String.format("SELECT sprinkler_group, sprinkler_id, location, is_on, is_functional, water_flux FROM %s", Utils.SPRINKLER_TABLE);
		ResultSet resultSet = DBHelper.queryDatabase(dbConnection, query);
		if(resultSet == null)
			return;
		try{
			while(resultSet.next()){
				String group = resultSet.getString("sprinkler_group");
				String sprinklerId = resultSet.getString("sprinkler_id");
				Boolean isTurnedOn = resultSet.getBoolean("is_on");
				Boolean isFunctional = resultSet.getBoolean("is_functional");
				String location = resultSet.getString("location");
				double waterFlux = Double.valueOf(resultSet.getString("water_flux"));
				int xLocation = Integer.valueOf(location.split(",")[0]);
				int yLocation = Integer.valueOf(location.split(",")[1]);
				Sprinkler sprinkler = new Sprinkler(group, sprinklerId, isTurnedOn, isFunctional, 
						waterFlux, xLocation, yLocation);
				if(!groupSprinklerMap.containsKey(group)){
					groupSprinklerMap.put(group, new ArrayList<Sprinkler>());
				}
				ArrayList<Sprinkler> sprinklers = groupSprinklerMap.get(group);
				sprinklers.add(sprinkler);
				groupSprinklerMap.put(group, sprinklers);
				idSprinklerMap.put(sprinklerId, sprinkler);
				
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void startSprinklerThreads(){
		for(Sprinkler sprinkler : getAllSprinklers())
			new Thread(sprinkler).start();
	}
	
	/**
	 * Add observer to each sprinkler
	 * @param observer
	 */
	public void addObserverToSprinklers(Observer observer){
		for(Sprinkler sprinkler : getAllSprinklers())
			sprinkler.addObserver(observer);
	}
	
	public void storeSprinklerStatus(){
		if(!isDbConnected())
			return;
		for(String sprinklerId : idSprinklerMap.keySet()){
			Sprinkler sprinkler = idSprinklerMap.get(sprinklerId);
			String update = String.format("UPDATE %s set water_flux=\"%s\", is_on=%d, is_functional=%d where sprinkler_group=\"%s\" and sprinkler_id=\"%s\"",
					Utils.SPRINKLER_TABLE, sprinkler.getWaterFlux(), sprinkler.isSprinklerOn() ? 1 :0,
							sprinkler.isSprinklerFunctional() ? 1 : 0,
							sprinkler.getSprinklerGroup(), sprinkler.getSprinklerId());
			DBHelper.updateDatabase(dbConnection, update);
		}
	}
	
	public void closeDbConnection(){
		DBHelper.closeDbConnection(dbConnection);
	}
	
	
	public boolean isSprinklerTurnedOn(String sprinklerId){
		Sprinkler sprinkler = idSprinklerMap.get(sprinklerId);
		if(sprinkler == null)
			return false;
		return sprinkler.isSprinklerOn();
	}
	
	public boolean isSprinklerFunctional(String sprinklerId){
		Sprinkler sprinkler = idSprinklerMap.get(sprinklerId);
		if(sprinkler == null)
			return false;
		return sprinkler.isSprinklerFunctional();
	}
	
	public void setSprinklerEnabled(String sprinklerId, boolean enable){
		Sprinkler sprinkler = idSprinklerMap.get(sprinklerId);
		if(sprinkler == null)
			return;
		sprinkler.setSprinklerEnabled(enable);
	}
	
	public void setGroupSprinklerEnabled(String group, boolean enable){
		Collection<Sprinkler> sprinklers = groupSprinklerMap.get(group);
		if(sprinklers == null)
			return;
		
		for(Sprinkler sprinkler : sprinklers)
			sprinkler.setSprinklerEnabled(enable);
	}
	
	/**
	 * Set weekly plan and temperature plan to each sprinkler
	 * @param weeklyPlan
	 * @param temperaturePlan
	 */
	public void setSprinklerPlan(WeeklyPlan weeklyPlan, TemperaturePlan temperaturePlan){
		for(Sprinkler sprinkler : getAllSprinklers()){
			sprinkler.setWeeklyPlan(weeklyPlan);
			sprinkler.setTemperaturePlan(temperaturePlan);
		}
	}
	
	public void setSprinklerDatetimeSimulator(DateTimeSimulator dateTimeSimulator){
		for(Sprinkler sprinkler : getAllSprinklers()){
			sprinkler.setDateTimeSimulator(dateTimeSimulator);;
		}
	}
	
	public void setSprinklerTemperatureSensor(TemperatureSimulator temperatureSimulator){
		for(String group : groupSprinklerMap.keySet()){
			TemperatureSensor temperatureSensor = new TemperatureSensor(group, temperatureSimulator);
			temperatureSensor.start();
			for(Sprinkler sprinkler : groupSprinklerMap.get(group)){
				sprinkler.setTemperatureSensor(temperatureSensor);
			}
		}
	}
	
	public ArrayList<String> getSprinklerGroups(){
		ArrayList<String> groups = new ArrayList<String>();
		for(String group : this.groupSprinklerMap.keySet())
			groups.add(group);
		return groups;
	}
	
	/**
	 * Get all the sprinklers under a given group
	 * @param group the group name
	 * @return An ArrayList of Sprinkler
	 */
	public ArrayList<Sprinkler> getGroupSprinklers(String group) {
		if(group == null)
			return new ArrayList<Sprinkler>();
		return groupSprinklerMap.get(group);
	}
	
	/**
	 * Get the sprinkler with a given Id
	 * @param id the id of the sprinkler
	 * @return the Sprinkler instance
	 */
	public Sprinkler getSprinklerFromId(String id){
		if(id == null)
			return null;
		return idSprinklerMap.get(id);
	}

	
	/**
	 * Get each sprinkler water usage per month
	 * @return Water usage for each sprinkler per month
	 */
	public HashMap<String, HashMap<Integer, Double>> getAllSprinkerMonthlyWaterUsage(){
		HashMap<String, HashMap<Integer, Double>> sprinklerMonthlyWaterUsage = new HashMap<String, HashMap<Integer, Double>>();
		
		for(String sprinklerId : idSprinklerMap.keySet()){
			Sprinkler sprinkler = idSprinklerMap.get(sprinklerId);
			sprinklerMonthlyWaterUsage.put(sprinklerId, sprinkler.getWaterUsage().getMonthlyWaterUsage());
		}
		return sprinklerMonthlyWaterUsage;
	}
	
	/**
	 * Get one sprinkler's monthly water usage
	 * @param sprinklerId the sprinkler id for one sprinkler
	 * @return A HashMap containing water usage
	 */
	public HashMap<Integer, Double> getSprinkerMonthlyWaterUsage(String sprinklerId){
		Sprinkler sprinkler = idSprinklerMap.get(sprinklerId);
		if(sprinkler == null)
			return null;
		return sprinkler.getWaterUsage().getMonthlyWaterUsage();
	}
	
	
	/**
	 * Get each sprinkler water usage under a given month
	 * @param month the given month
	 * @return Water usage for each sprinkler under a given month
	 */
	public HashMap<String, Double> getAllSprinklerOneMonthWaterUsage(String month){
		HashMap<String, Double> sprinklerOneMonthWaterUsage = new HashMap<String, Double>();
		
		for(String sprinklerId : idSprinklerMap.keySet()){
			Sprinkler sprinkler = idSprinklerMap.get(sprinklerId);
			HashMap<Integer, Double> allMonthWaterUsage = sprinkler.getWaterUsage().getMonthlyWaterUsage();
			sprinklerOneMonthWaterUsage.put(sprinklerId, allMonthWaterUsage.get(month) == null ? 0.0 : allMonthWaterUsage.get(month));
		}
		return sprinklerOneMonthWaterUsage;
	}
	
	/**
	 * Get each group water usage per month
	 * @returnWater usage for each group per month
	 */
	public HashMap<String, HashMap<Integer, Double>> getGroupMonthlyWaterUsage(){
		HashMap<String, HashMap<Integer, Double>> groupMonthlyWaterUsage = new HashMap<String, HashMap<Integer, Double>>();
		
		for(String sprinklerId : idSprinklerMap.keySet()){
			Sprinkler sprinkler = idSprinklerMap.get(sprinklerId);
			if(!groupMonthlyWaterUsage.containsKey(sprinkler.getSprinklerGroup())){
				groupMonthlyWaterUsage.put(sprinkler.getSprinklerGroup(), new HashMap<Integer, Double>());
			}
			HashMap<Integer, Double> monthlyWaterUsage = groupMonthlyWaterUsage.get(sprinkler.getSprinklerGroup());
			HashMap<Integer, Double> sprinklerMontlyWaterUsage = sprinkler.getWaterUsage().getMonthlyWaterUsage();
			for(Integer month : sprinklerMontlyWaterUsage.keySet()){
				if(!monthlyWaterUsage.containsKey(month))
					monthlyWaterUsage.put(month, 0.0);
				double waterUsage = monthlyWaterUsage.get(month);
				waterUsage += sprinklerMontlyWaterUsage.get(month) == null ? 0.0 : sprinklerMontlyWaterUsage.get(month);
				monthlyWaterUsage.put(month, waterUsage);
			}
			groupMonthlyWaterUsage.put(sprinkler.getSprinklerGroup(), monthlyWaterUsage);
		}
		return groupMonthlyWaterUsage;
	}
	
	public Collection<Sprinkler> getAllSprinklers() {
		return idSprinklerMap.values();
	}
	
	public ArrayList<String> getAllSprinklerIds(){
		ArrayList<String> sprinklerIds = new ArrayList<String>();
		for(String sprinklerId : idSprinklerMap.keySet())
			sprinklerIds.add(sprinklerId);
		Collections.sort(sprinklerIds);
		return sprinklerIds;
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
