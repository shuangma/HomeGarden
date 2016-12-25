package com.hummingbee.garden.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.hummingbee.garden.view.plan.PlanPanel;

public class WeeklyPlanGroupChooseListener implements ActionListener{
	private PlanPanel planPanel = null;
	
	public WeeklyPlanGroupChooseListener(PlanPanel planPanel){
		this.planPanel = planPanel;
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		planPanel.showWeeklyPlan();
	}

}
