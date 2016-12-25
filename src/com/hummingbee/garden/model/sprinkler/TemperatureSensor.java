package com.hummingbee.garden.model.sprinkler;

import com.hummingbee.garden.model.simulation.TemperatureSimulator;

/**
 * The TemperatureSensor class represents a temperature sensor, each sprinkler group owns one sensor
 * This temperature sensor will monitor the current temperature
 * @author Shuang Ma
 *
 */

public class TemperatureSensor extends Thread{
	private double curTemperature = 85;
	private TemperatureSimulator temperatureSimulator = null;
	private String group;
	
	public TemperatureSensor(String group, TemperatureSimulator temperatureSimulator){
		this.group = group;
		this.temperatureSimulator = temperatureSimulator;
	}
	
	public String getGroup(){
		return group;
	}
	
	public double getCurTemperature(){
		return curTemperature;
	}
	
	public void run(){
		// This sensor monitors current temperature every 1 seconds
		while(true){
			try {
				Thread.sleep(1*1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			curTemperature = this.temperatureSimulator.getGroupTemperature(group);
		}
	}
}
