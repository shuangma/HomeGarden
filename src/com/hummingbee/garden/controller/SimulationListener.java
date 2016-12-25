package com.hummingbee.garden.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.hummingbee.garden.common.Utils;
import com.hummingbee.garden.model.simulation.DateTimeSimulator;
import com.hummingbee.garden.model.simulation.TemperatureSimulator;
import com.hummingbee.garden.view.simulation.SimulationDialog;

public class SimulationListener implements ActionListener{

	private TemperatureSimulator temperatureSimulator;
	private DateTimeSimulator dateTimeSimulator;
	private SimulationDialog simulationDialog;

	public SimulationListener(TemperatureSimulator temperatureSimulator, DateTimeSimulator dateTimeSimulator,
			SimulationDialog simulationDialog){
		this.temperatureSimulator = temperatureSimulator;
		this.dateTimeSimulator = dateTimeSimulator;
		this.simulationDialog = simulationDialog;
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		String actionCmd = event.getActionCommand();
		if(actionCmd.equals(SimulationDialog.TEMPERATURE_SUBMIT)){
			String group = this.simulationDialog.getGroup();
			double temperature = this.simulationDialog.getTemperature();
			temperatureSimulator.setGroupTemperature(group, temperature);
			Utils.showInfoMessageDialog(null, "Temperature simulation submitted!");
		}else if(actionCmd.equals(SimulationDialog.DATE_SUBMIT)){
			String dateStr = this.simulationDialog.getDate();
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
			Date date = null;
			try{
				date = format.parse(dateStr);
				dateTimeSimulator.setCurDateTime(date);
				Utils.showInfoMessageDialog(null, "Date simulation submitted!");
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}

}
