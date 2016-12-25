package com.hummingbee.garden.view.waterusage;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JTabbedPane;

import com.hummingbee.garden.common.Utils;
import com.hummingbee.garden.model.sprinkler.SprinklerBank;

public class WaterUsagePanel extends JTabbedPane implements Runnable{
	public static final int WIDTH = Utils.FRAME_WIDTH/2;
	public static final int HEIGHT = Utils.FRAME_HEIGHT/2;
	private static final String TITLE = "Water Usage";
	
	private WaterUsageMonthlyBarPanel monthlyBarPanel = null;
	private WaterUsageMonthlyLinePanel monthlyLinePanel = null;
	
	public WaterUsagePanel(SprinklerBank sprinklerBank){
		Utils.initMainPanelBorder(this, TITLE);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setFont(Utils.getFormatFont(Font.BOLD, 12));
		
		monthlyBarPanel = new WaterUsageMonthlyBarPanel(sprinklerBank);
		addTab(WaterUsageMonthlyBarPanel.TITLE, monthlyBarPanel);
		
		monthlyLinePanel = new WaterUsageMonthlyLinePanel(sprinklerBank);
		addTab(WaterUsageMonthlyLinePanel.TITLE, monthlyLinePanel);
		
	}

	@Override
	public void run() {
		while(true){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			monthlyBarPanel.update();
			monthlyLinePanel.update();
		}
	}
	
	public WaterUsageMonthlyBarPanel getMonthlyBarPanel(){
		return monthlyBarPanel;
	}
	
	public WaterUsageMonthlyLinePanel getMontlyLinePanel(){
		return monthlyLinePanel;
	}
}
