package com.hummingbee.garden.view.overview;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import com.hummingbee.garden.common.Utils;
import com.hummingbee.garden.controller.SprinklerLabelListener;
import com.hummingbee.garden.model.sprinkler.SprinklerBank;
import com.hummingbee.garden.view.operation.OperationPanel;
import com.hummingbee.garden.view.sprinkler.SprinklerMapPanel;
import com.hummingbee.garden.view.sprinkler.SprinklerStatusPanel;
import com.hummingbee.garden.view.waterusage.WaterUsagePanel;

public class OverViewPanel extends JPanel{
	public static final String TITLE = "Overview";
	
	private SprinklerMapPanel sprinklerMapPanel;
	private WaterUsagePanel waterUsagePanel;
	private OperationPanel operationPanel;
	private SprinklerStatusPanel sprinklerStatusPanel;
	
	private SprinklerBank sprinklerBank;
	
	public OverViewPanel(SprinklerBank sprinklerBank){
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		this.sprinklerBank = sprinklerBank;
		
		SprinklerLabelListener sprinklerLabelListener = new SprinklerLabelListener(sprinklerBank);
		sprinklerMapPanel = new SprinklerMapPanel(sprinklerBank.getAllSprinklers(), sprinklerLabelListener);
		add(sprinklerMapPanel);
		
		add(Box.createHorizontalStrut(10));
		
		JPanel innerPanel = createInnerPanel();
		
		add(innerPanel);
	}
	
	public SprinklerMapPanel getSprinklerMapPanel(){
		return sprinklerMapPanel;
	}
	
	public SprinklerStatusPanel getSprinklerStatusPanel(){
		return sprinklerStatusPanel;
	}
	
	public OperationPanel getOperationPanel(){
		return operationPanel;
	}
	
	public WaterUsagePanel getWaterUsagePanel(){
		return waterUsagePanel;
	}
	
	private JPanel createInnerPanel(){
		JPanel innerPanel = new JPanel();
		innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
		innerPanel.setPreferredSize(new Dimension(Utils.FRAME_WIDTH/2, Utils.FRAME_HEIGHT));
		
		// Init the water usage panel and start the thread to check water usage
		waterUsagePanel = new WaterUsagePanel(sprinklerBank);
		new Thread(waterUsagePanel).start();
		
		operationPanel = new OperationPanel(sprinklerBank.getSprinklerGroups());
		sprinklerStatusPanel = new SprinklerStatusPanel(sprinklerBank);
		
		innerPanel.add(waterUsagePanel);
		innerPanel.add(operationPanel);
		innerPanel.add(sprinklerStatusPanel);
		
		return innerPanel;
	}
}