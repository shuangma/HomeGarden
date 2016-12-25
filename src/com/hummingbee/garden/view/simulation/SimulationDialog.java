package com.hummingbee.garden.view.simulation;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.hummingbee.garden.common.Utils;
import com.hummingbee.garden.model.simulation.DateTimeSimulator;

public class SimulationDialog extends JDialog{
	private static final String TITLE = "Simulation Panel";
	
	public static final String TEMPERATURE_SUBMIT = "temperature";
	public static final String DATE_SUBMIT = "date";
	
	private static final int WIDTH = 480;
	private static final int HEIGHT = 280;
	
	private ArrayList<String> groups;
		
	private JComboBox groupComboBox;
	private JTextField temperatureField;
	private JButton temperatureSubmitButton;
	
	private JTextField yearField;
	private JTextField monthField;
	private JTextField dayField;
	private JTextField hourField;
	private JTextField minField;
	
	private JButton dateSubmitButton;
	
	public SimulationDialog(ArrayList<String> groups, DateTimeSimulator dateTimeSimulator){
		this.groups = groups;
		
		this.setTitle(TITLE);
		this.setFont(Utils.getFormatFont(Font.BOLD, 16));
		this.setSize(new Dimension(WIDTH, HEIGHT));
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setLocation(new Point(WIDTH, HEIGHT));
		
		initComps(dateTimeSimulator);
	}
	
	private void initComps(DateTimeSimulator dateTimeSimulator){
		JPanel temperaturePanel = new JPanel();
		Utils.initSubPanelBorder(temperaturePanel, "Set Temperature");
		temperaturePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		JLabel groupLabel = new JLabel("Sprinkler Group:");
		groupLabel.setFont(Utils.getFormatFont(Font.BOLD, 16));
		temperaturePanel.add(groupLabel);
		
		Collections.sort(groups);
		groupComboBox = new JComboBox(groups.toArray());
		groupComboBox.setEditable(false);
		groupComboBox.setFont(Utils.getFormatFont(Font.PLAIN, 16));
		temperaturePanel.add(groupComboBox);
		
		temperatureField = new JTextField(5);
		temperatureField.setFont(Utils.getFormatFont(Font.BOLD, 16));
		temperaturePanel.add(temperatureField);
		
		temperatureSubmitButton = new JButton("Submit");
		temperatureSubmitButton.setFont(Utils.getFormatFont(Font.BOLD, 16));
		temperatureSubmitButton.setActionCommand(TEMPERATURE_SUBMIT);
		temperaturePanel.add(temperatureSubmitButton);
		
		JPanel datePanel = new JPanel();
		Utils.initSubPanelBorder(datePanel, "Set Date");
		datePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		Date curDate = dateTimeSimulator.getCurDateTime();
		
		monthField = new JTextField(5);
		monthField.setText(String.valueOf(Utils.getMonthFromDate(curDate)));
		monthField.setFont(Utils.getFormatFont(Font.BOLD, 16));
		datePanel.add(monthField);
		
		JLabel slashLabel1 = new JLabel("/");
		slashLabel1.setFont(Utils.getFormatFont(Font.BOLD, 16));
		datePanel.add(slashLabel1);
		
		dayField = new JTextField(5);
		dayField.setText(String.valueOf(Utils.getDayFromDate(curDate)));
		dayField.setFont(Utils.getFormatFont(Font.BOLD, 16));
		datePanel.add(dayField);
		
		JLabel slashLabel2 = new JLabel("/");
		slashLabel2.setFont(Utils.getFormatFont(Font.BOLD, 16));
		datePanel.add(slashLabel2);
		
		yearField = new JTextField(5);
		yearField.setText(String.valueOf(Utils.getYearFromDate(curDate)));
		yearField.setFont(Utils.getFormatFont(Font.BOLD, 16));
		datePanel.add(yearField);
		
		hourField = new JTextField(5);
		hourField.setText(String.valueOf(Utils.getHourFromDate(curDate)));
		hourField.setFont(Utils.getFormatFont(Font.BOLD, 16));
		datePanel.add(hourField);

		JLabel colonLabel2 = new JLabel(":");
		colonLabel2.setFont(Utils.getFormatFont(Font.BOLD, 16));
		datePanel.add(colonLabel2);
		
		minField = new JTextField(5);
		minField.setText(String.valueOf(Utils.getMinuteFromDate(curDate)));
		minField.setFont(Utils.getFormatFont(Font.BOLD, 16));
		datePanel.add(minField);
		
		
		dateSubmitButton = new JButton("Submit");
		dateSubmitButton.setFont(Utils.getFormatFont(Font.BOLD, 16));
		dateSubmitButton.setActionCommand(DATE_SUBMIT);
		datePanel.add(dateSubmitButton);
		
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		this.getContentPane().add(Box.createVerticalStrut(20));
		
		this.getContentPane().add(temperaturePanel);
		this.getContentPane().add(datePanel);
		this.getContentPane().add(Box.createVerticalGlue());
	}
	
	public void addSimulationListener(ActionListener actionListener){
		temperatureSubmitButton.addActionListener(actionListener);
		dateSubmitButton.addActionListener(actionListener);
	}
	
	public String getGroup(){
		return groupComboBox.getSelectedItem().toString();
	}
	
	public double getTemperature(){
		return Double.valueOf(temperatureField.getText());
	}
	
	public String getDate(){
		return String.format("%s-%s-%s %s:%s", yearField.getText().trim(), monthField.getText().trim(), 
				dayField.getText().trim(), hourField.getText().trim(), minField.getText().trim());
	}
}
