package com.hummingbee.garden.model.sprinkler;

import java.util.ArrayList;
import java.util.Date;
import java.util.Observable;

import com.hummingbee.garden.common.Utils;
import com.hummingbee.garden.model.simulation.DateTimeSimulator;

public class Sprinkler extends Observable implements Runnable {
	public static final String SPRINKLER_OBSERVABLE = "sprinkler";
	
	private static final double NORMAL_WATER_FLUX = 50.0;
	private static final double BIGGER_WATER_FLUX = 75.0;
	
	private String group;
	private String sprinklerId;
	
	private int xLocation = 0;
	private int yLocation = 0;
	
	private double normalWaterFlux = 0.0;
	private double waterFlux = 0.0;
	
	private boolean isTurnedOn = false;
	private boolean isFunctional = true;
	
	private boolean isEnabled = true;
	
	private DateTimeSimulator dateTimeSimulator = null;
	private TemperatureSensor temperatureSensor = null;
	
	private WeeklyPlan weeklyPlan = null;
	private TemperaturePlan temperaturePlan = null;
	private WaterUsage waterUsage = null;
	
	public Sprinkler(String group, String sprinklerId, boolean isTurnedOn, boolean isFunctional, 
			double waterFlux, int xLocation, int yLocation) {
		this.group = group;
		this.sprinklerId = sprinklerId;
		this.isTurnedOn = isTurnedOn;
		this.isFunctional = isFunctional;
		
		this.waterFlux = waterFlux >= BIGGER_WATER_FLUX ? NORMAL_WATER_FLUX : waterFlux;
		this.normalWaterFlux = waterFlux;
		
		this.xLocation = xLocation;
		this.yLocation = yLocation;
		
		this.waterUsage = new WaterUsage(group, sprinklerId);
	}
	
	public int getXLocation(){
		return xLocation;
	}
	
	public int getYLocation(){
		return yLocation;
	}
	
	public void setWeeklyPlan(WeeklyPlan weeklyPlan){
		this.weeklyPlan = weeklyPlan;
	}
	
	public void setTemperaturePlan(TemperaturePlan temperaturePlan){
		this.temperaturePlan = temperaturePlan;
	}
	
	public void setDateTimeSimulator(DateTimeSimulator dateTimeSimulator){
		this.dateTimeSimulator = dateTimeSimulator;
	}
	
	public void setTemperatureSensor(TemperatureSensor temperatureSensor){
		this.temperatureSensor = temperatureSensor;
	}
	
	public void turnOnSprinkler(boolean userControl) {
		isTurnedOn = true;
		this.setSprinklerEnabled(true);
	}
	
	public void turnOffSprinkler(boolean userControl) {
		isTurnedOn = false;
		this.setSprinklerEnabled(false);
	}
	
	private void notifyChanges(){
		setChanged();
		notifyObservers(SPRINKLER_OBSERVABLE);
	}
	
	/**
	 * Sprinkler itself implements Runnable interface to function as a thread,
	 * it checks the plan, sprinkler status and calculate the water usage in the background
	 */
	public void run() {
		while (true) {
			try {
				// Apply temperature plan and weekly plan
				// If the temperature is too low, turn off the sprinkler, however, 
				// if the temperature is too high, then turn it on and set the waterflux to a bigger value
				if(isTemperatureTooLow()){
					turnOffSprinkler(false);
					waterFlux = normalWaterFlux;
					notifyChanges();
				}else if(isTemperatureTooHigh()){
					turnOnSprinkler(false);
					waterFlux = BIGGER_WATER_FLUX;
					notifyChanges();
				}else{
					// If the temperature is normal, we should check the weekly plan and 
					// see if there is a need to turn on the sprinkler and set the waterflux to last recorded one
					// while if the sprinkler is currently disabled by the user, no need to apply the weekly plan
					if(this.isEnabled && isSprinklerShouldWorkOnWeeklyPlan()){
						turnOnSprinkler(false);
						waterFlux = normalWaterFlux;
						notifyChanges();
					}
				}
				
				Thread.sleep(1000);
				if (isTurnedOn) {
					Date curDate = dateTimeSimulator.getCurDateTime();
					String today = Utils.formatOnlyDate(curDate);
					waterUsage.addWaterUsage(today, waterFlux / 3600);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	// Check if this sprinkler should turn on according to the weekly plan 
	private boolean isSprinklerShouldWorkOnWeeklyPlan(){
		if(weeklyPlan == null || dateTimeSimulator == null)
			return false;
		Date today = dateTimeSimulator.getCurDateTime();
		String dayOfWeek = Utils.getDayOfWeekFromDate(today).toLowerCase();
		if(dayOfWeek == null)
			return false;
		int curHour = Utils.getHourFromDate(today);
		int curMinute = Utils.getMinuteFromDate(today);
		
		ArrayList<String> groupDayPlan = this.weeklyPlan.getGroupDayPlan(this.group, dayOfWeek);
		if(groupDayPlan == null)
			return false;
		for(String dayPlan : groupDayPlan){
			String[] startEndTime = dayPlan.split("-");
			if(startEndTime.length != 2)
				continue;
			String[] startTimeItems = startEndTime[0].split(":");
			String[] endTimeItems = startEndTime[1].split(":");
			if(startTimeItems.length != 2 || endTimeItems.length != 2)
				continue;
			int startMins = Integer.valueOf(startTimeItems[0]) * 60 + Integer.valueOf(startTimeItems[1]);
			int endMins = Integer.valueOf(endTimeItems[0]) * 60 + Integer.valueOf(endTimeItems[1]);
			int curMins = curHour * 60 + curMinute;
			if(startMins <= curMins && curMins <= endMins) 
				return true;
		}
		return false;
	}
	
	// Check if current temperature is lower than the given plan
	private boolean isTemperatureTooLow(){
		if(temperaturePlan == null || temperatureSensor == null)
			return false;
		double curTemperature = temperatureSensor.getCurTemperature();
		if(curTemperature <= temperaturePlan.getLowTemperature())
			return true;
		return false;
	}
	
	// Check if current temperature is higher than the given plan
	private boolean isTemperatureTooHigh(){
		if(temperaturePlan == null || temperatureSensor == null)
			return false;
		double curTemperature = temperatureSensor.getCurTemperature();
		if(curTemperature >= temperaturePlan.getHighTemperature())
			return true;
		return false;
	}
	
	public String getSprinklerId() {
		return sprinklerId;
	}
	
	public String getSprinklerGroup() {
		return group;
	}
	
	public double getWaterFlux(){
		return waterFlux;
	}
	
	public void setWaterFlux(double waterFlux){
		this.waterFlux = waterFlux;
	}
	
	public boolean isSprinklerOn(){
		return isTurnedOn;
	}
	
	public boolean isSprinklerFunctional(){
		return isFunctional;
	}
	
	public WaterUsage getWaterUsage(){
		return waterUsage;
	}
	
	public void setSprinklerEnabled(boolean enable){
		isEnabled = enable;
	}
	
}
