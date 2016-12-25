package com.hummingbee.garden.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.table.TableModel;

import com.hummingbee.garden.common.Utils;
import com.hummingbee.garden.view.plan.PlanPanel;

public class WeeklyPlanButtonListener implements ActionListener{
	
	private PlanPanel planPanel = null;
	
	public WeeklyPlanButtonListener(PlanPanel planPanel){
		this.planPanel = planPanel;
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getActionCommand().equals(PlanPanel.TEMPERATURE_SUBMIT)){
			planPanel.submitTemperaturePlan();
			Utils.showInfoMessageDialog(null, "Temperature plan submitted!");
		}else if(event.getActionCommand().equals(PlanPanel.WEEKLY_PLAN_ADD)){
			planPanel.addRow();
		}else if(event.getActionCommand().equals(PlanPanel.WEEKLY_PLAN_SUBMIT)){
			planPanel.submitWeeklyPlan();
			Utils.showInfoMessageDialog(null, "Weekly plan submitted!");
		}
	}

}
