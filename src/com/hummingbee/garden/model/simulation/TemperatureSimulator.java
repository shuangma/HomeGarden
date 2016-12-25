package com.hummingbee.garden.model.simulation;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 
 * @author Shuang Ma
 * The Temperature class represents current temperature, can be simulated by a user
 */

public class TemperatureSimulator{
	private HashMap<String, Double> groupTemperature = new HashMap<String, Double>();
	
	public TemperatureSimulator(ArrayList<String> groups){
		for(String group : groups)
			groupTemperature.put(group, 85.0);
	}
	
	public double getGroupTemperature(String group){
		if(group == null || !groupTemperature.containsKey(group))
			return 0.0;
		return groupTemperature.get(group);
	}
	
	public void setGroupTemperature(String group, double temperature){
		if(group == null || group.equals(""))
			return;
		groupTemperature.put(group, temperature);
	}
}
