package com.hummingbee.garden.model.sprinkler;

import java.util.Observable;

public class TemperaturePlan extends Observable{
	private double lowTemperature = 55.0;
	private double highTemperature = 100.0;
	
	public void setTemperaturePlan(double lowTemperature, double highTemperature){
		this.lowTemperature = lowTemperature;
		this.highTemperature = highTemperature;
		
		notifyChanges();
	}
	
	private void notifyChanges(){
		setChanged();
		notifyObservers();
	}
	
	public double getLowTemperature(){
		return lowTemperature;
	}
	
	public double getHighTemperature(){
		return highTemperature;
	}
}
