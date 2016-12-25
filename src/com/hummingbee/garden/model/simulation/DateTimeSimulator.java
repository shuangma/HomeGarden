package com.hummingbee.garden.model.simulation;

import java.util.Date;
import java.util.Observable;

public class DateTimeSimulator extends Observable{
	private Date curDateTime = new Date();
	
	public void publish(){
		
		setChanged();
		notifyObservers();
	}
	
	public Date getCurDateTime(){
		return curDateTime;
	}
	
	public void setCurDateTime(Date curDateTime){
		this.curDateTime = curDateTime;
	}
}
