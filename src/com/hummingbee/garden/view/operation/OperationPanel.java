package com.hummingbee.garden.view.operation;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.hummingbee.garden.common.Utils;

public class OperationPanel extends JPanel{
	public static final String TRUN_ON_CMD = "turnon";
	public static final String TRUN_OFF_CMD = "turnff";
	public static final String SIMULATION_CMD = "simulation";
	
	public static final int WIDTH = Utils.FRAME_WIDTH/2;
	public static final int HEIGHT = 80;
	
	private static final String TITLE = "Operation";
	
	private JComboBox groupComboBox = null;
	private JButton turnOnButton = null;
	private JButton turnOffButton = null;
	private JButton simulationButton = null;
	
	public OperationPanel(ArrayList<String> groups){
		setLayout(new FlowLayout());
		Utils.initMainPanelBorder(this, TITLE);
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		
		JLabel groupLabel = new JLabel("Sprinkler Group:");
		groupLabel.setFont(Utils.getFormatFont(Font.BOLD, 16));
		add(groupLabel);
		
		Collections.sort(groups);
		groupComboBox = new JComboBox(groups.toArray());
		groupComboBox.setEditable(false);
		add(groupComboBox);
		groupComboBox.setFont(Utils.getFormatFont(Font.PLAIN, 16));
		
		turnOnButton = new JButton("Turn ON");
		turnOnButton.setActionCommand(TRUN_ON_CMD);
		turnOnButton.setFont(Utils.getFormatFont(Font.PLAIN, 16));
		add(turnOnButton);
		
		
		turnOffButton = new JButton("Turn OFF");
		turnOffButton.setActionCommand(TRUN_OFF_CMD);
		turnOffButton.setFont(Utils.getFormatFont(Font.PLAIN, 16));
		add(turnOffButton);
		
		simulationButton = new JButton("Simulation");
		simulationButton.setActionCommand(SIMULATION_CMD);
		simulationButton.setFont(Utils.getFormatFont(Font.PLAIN, 16));
		add(simulationButton);
	}
	
	public JComboBox getGroupComobBox(){
		return this.groupComboBox;
	}
	
	public void addOperationActionListener(ActionListener actionListener){
		turnOnButton.addActionListener(actionListener);
		turnOffButton.addActionListener(actionListener);
		simulationButton.addActionListener(actionListener);
	}
	
	
}
