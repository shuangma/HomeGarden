package com.hummingbee.garden.view.sprinkler;

import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;


public class SprinklerSetView extends JPanel {
	private JPanel panelNorth = new JPanel();
	private JPanel panelSouth = new JPanel();
	private JPanel panelEast = new JPanel();
	private JPanel panelWest = new JPanel();
	
	private JButton buttonNor;
	private JButton buttonSou;
	private JButton buttonEast;
	private JButton buttonWest;
	private JButton buttonDetail;
	
	public SprinklerSetView() {
		setBorder(new TitledBorder("Setting Part"));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JLabel labelN = new JLabel("north");
		buttonNor = new JButton("on");
		buttonNor.setActionCommand("north");
		panelNorth.add(labelN);
		panelNorth.add(buttonNor);
		
		add(panelNorth);
		
		JLabel labelE = new JLabel("east");
		buttonEast = new JButton("on");
		panelEast.add(labelE);
		panelEast.add(buttonEast);
		buttonEast.setActionCommand("east");
		
		add(panelEast);
		
		JLabel labelW = new JLabel("west");
		buttonWest = new JButton("on");
		panelWest.add(labelW);
		panelWest.add(buttonWest);
		buttonWest.setActionCommand("west");
		
		add(panelWest);
		
		JLabel labelS = new JLabel("south");
		buttonSou = new JButton("on");
		panelSouth.add(labelS);
		panelSouth.add(buttonSou);
		buttonSou.setActionCommand("south");
		
		add(panelSouth);
		
		buttonDetail = new JButton("get details");
		buttonDetail.setActionCommand("dtl");
		add(buttonDetail);
	}
	
	public void addActionListener(ActionListener listener) {
		buttonNor.addActionListener(listener);
		buttonSou.addActionListener(listener);
		buttonEast.addActionListener(listener);
		buttonWest.addActionListener(listener);
		buttonDetail.addActionListener(listener);
	}
}
