package com.hummingbee.garden.controller;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JDialog;

import com.hummingbee.garden.common.Utils;
import com.hummingbee.garden.model.simulation.DateTimeSimulator;
import com.hummingbee.garden.model.simulation.TemperatureSimulator;
import com.hummingbee.garden.model.sprinkler.SprinklerBank;
import com.hummingbee.garden.view.operation.OperationPanel;
import com.hummingbee.garden.view.simulation.SimulationDialog;

public class OperationController implements ActionListener{

	private SprinklerBank sprinklerBank = null;
	private JComboBox groupComboBox = null;
	private TemperatureSimulator temperatureSimulator;
	private DateTimeSimulator dateTimeSimulator;
	
	public OperationController(SprinklerBank sprinklerBank, JComboBox groupComboBox,
			TemperatureSimulator temperatureSimulator, DateTimeSimulator dateTimeSimulator){
		this.sprinklerBank = sprinklerBank;
		this.groupComboBox = groupComboBox;
		this.temperatureSimulator = temperatureSimulator;
		this.dateTimeSimulator = dateTimeSimulator;
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		String actionCmd = event.getActionCommand();
		String group = groupComboBox.getSelectedItem().toString();
		if(actionCmd.equals(OperationPanel.TRUN_ON_CMD)){
			sprinklerBank.turnOnGroupSprinkler(group, true);
			Utils.showInfoMessageDialog(null, String.format("Sprinklers under group %s turned on!", group));
		}else if(actionCmd.equals(OperationPanel.TRUN_OFF_CMD)){
			sprinklerBank.turnOffGroupSprinkler(group, true);
			Utils.showInfoMessageDialog(null, String.format("Sprinklers under group %s turned off!", group));
		}else if(actionCmd.equals(OperationPanel.SIMULATION_CMD)){
			SimulationDialog simulationDialog = new SimulationDialog(this.sprinklerBank.getSprinklerGroups(), dateTimeSimulator);
			SimulationListener simulationListener = new SimulationListener(temperatureSimulator, dateTimeSimulator, simulationDialog);
			simulationDialog.addSimulationListener(simulationListener);
			simulationDialog.setVisible(true);
		}
	}

}
