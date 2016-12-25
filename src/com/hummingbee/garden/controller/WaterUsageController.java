package com.hummingbee.garden.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.hummingbee.garden.view.waterusage.WaterUsageMonthlyBarPanel;

public class WaterUsageController implements ActionListener{
	private WaterUsageMonthlyBarPanel monthlyBarPanel = null;
	
	public WaterUsageController(WaterUsageMonthlyBarPanel monthlyBarPanel){
		this.monthlyBarPanel = monthlyBarPanel;
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		String actionCmd = event.getActionCommand();
		if(actionCmd.equals(WaterUsageMonthlyBarPanel.SPRINKLER_CMD)){
			onChooseSprinkler();
		}
	}
	
	private void onChooseSprinkler(){
		monthlyBarPanel.update();
	}

}
