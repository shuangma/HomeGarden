package com.hummingbee.garden.view.plan;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.hummingbee.garden.common.CommonTableCellRender;
import com.hummingbee.garden.common.Utils;
import com.hummingbee.garden.model.sprinkler.TemperaturePlan;
import com.hummingbee.garden.model.sprinkler.WeeklyPlan;

public class PlanPanel extends JPanel{
	public static final String TITLE = "Plan Setting";
	public static final String TEMPERATURE_SUBMIT = "temperature";
	public static final String WEEKLY_PLAN_ADD = "weekly_plan_add";
	public static final String WEEKLY_PLAN_SUBMIT = "weekly_plan_submit";
	
	private WeeklyPlan weeklyPlan = null;
	private TemperaturePlan temperaturePlan = null;
	private ArrayList<String> groups = new ArrayList<String>();
	
	private static final String[] TABLE_HEADERS = {"SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"};
	
	private static Object[][] cellData = null;
	static DefaultTableModel tableMode = new DefaultTableModel(cellData, TABLE_HEADERS) {
	  public boolean isCellEditable(int row, int column) {
		  return true;
	  }
	};
	
	JTable weeklyPlanTable = new JTable(tableMode);
	private JComboBox groupComboBox = null;
	private JButton weeklyPlanAddButton = null;
	private JButton weeklyPlanSubmitButton = null;
	
	private JTextField lowTemperatureField = null;
	private JTextField highTemperatureField = null;
	private JButton	temperatureSubmitButton = null;
	
	public PlanPanel(WeeklyPlan weeklyPlan, TemperaturePlan temperaturePlan, ArrayList<String> groups){
		this.weeklyPlan = weeklyPlan;
		this.temperaturePlan = temperaturePlan;
		this.groups = groups;
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(Box.createVerticalStrut(10));
		
		add(initWeeklyPlanPanel());
		add(initTemperaturePlanPanel());
		
		showWeeklyPlan();
	}
	
	public JPanel initWeeklyPlanPanel(){
		JPanel mainPanel = new JPanel();
		Utils.initMainPanelBorder(mainPanel, "Weekly Plan");
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		
		// Group Selection panel
		JPanel selectionPanel = new JPanel();
		selectionPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		JLabel textLabel = new JLabel("Sprinkler Group");
		textLabel.setFont(Utils.getFormatFont(Font.BOLD, 16));
		selectionPanel.add(textLabel);
		
		Collections.sort(groups);
		groupComboBox = new JComboBox(groups.toArray());
		groupComboBox.setEditable(false);
		groupComboBox.setFont(Utils.getFormatFont(Font.PLAIN, 16));
		selectionPanel.add(groupComboBox);
		
		weeklyPlanAddButton = new JButton("Add Row");
		weeklyPlanAddButton.setFont(Utils.getFormatFont(Font.BOLD, 16));
		weeklyPlanAddButton.setActionCommand(WEEKLY_PLAN_ADD);
		selectionPanel.add(weeklyPlanAddButton);
		
		weeklyPlanSubmitButton = new JButton("Submit");
		weeklyPlanSubmitButton.setFont(Utils.getFormatFont(Font.BOLD, 16));
		weeklyPlanSubmitButton.setActionCommand(WEEKLY_PLAN_SUBMIT);
		selectionPanel.add(weeklyPlanSubmitButton); 
		
		mainPanel.add(selectionPanel);
		
		// Table Panel
		
		JScrollPane scrollPane = new JScrollPane(weeklyPlanTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		setTableStyle();
		mainPanel.add(scrollPane);
		
		mainPanel.add(Box.createVerticalGlue());
		
		return mainPanel;
	}
	
	private JPanel initTemperaturePlanPanel(){
		JPanel mainPanel = new JPanel();
		Utils.initMainPanelBorder(mainPanel, "Temperature Plan");
		mainPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		JLabel lowLabel = new JLabel("Low Temperature:");
		lowLabel.setFont(Utils.getFormatFont(Font.BOLD, 16));
		mainPanel.add(lowLabel);
		
		
		lowTemperatureField = new JTextField(5);
		lowTemperatureField.setText(String.valueOf(this.temperaturePlan.getLowTemperature()));
		lowTemperatureField.setFont(Utils.getFormatFont(Font.BOLD, 16));
		mainPanel.add(lowTemperatureField);
		
		
		JLabel highLabel = new JLabel("High Temperature:");
		highLabel.setFont(Utils.getFormatFont(Font.BOLD, 16));
		mainPanel.add(highLabel);
		
		highTemperatureField = new JTextField(5);
		highTemperatureField.setText(String.valueOf(this.temperaturePlan.getHighTemperature()));
		highTemperatureField.setFont(Utils.getFormatFont(Font.BOLD, 16));
		mainPanel.add(highTemperatureField);
		
		temperatureSubmitButton = new JButton("Submit");
		temperatureSubmitButton.setFont(Utils.getFormatFont(Font.BOLD, 16));
		temperatureSubmitButton.setActionCommand(TEMPERATURE_SUBMIT);
		mainPanel.add(temperatureSubmitButton);
		
		return mainPanel;
	}
	
	private void setTableStyle(){
		weeklyPlanTable.setFont(Utils.getFormatFont(Font.BOLD | Font.ITALIC, 12));
		weeklyPlanTable.getTableHeader().setFont(Utils.getFormatFont(Font.BOLD, 16));
		TableColumnModel columnModel = weeklyPlanTable.getColumnModel();  
        for (int i = 0, n = columnModel.getColumnCount(); i < n; i++)   
        {  
            TableColumn column = columnModel.getColumn(i);  
            column.setCellRenderer(new CommonTableCellRender());  
        }  
	}
	
	private String getGroup(){
		return groupComboBox.getSelectedItem().toString();
	}
	
	public void addGroupChooseListener(ActionListener actionListener){
		groupComboBox.addActionListener(actionListener);
	}
	
	public void addPlanButtonListener(ActionListener actionListener){
		temperatureSubmitButton.addActionListener(actionListener);
		weeklyPlanAddButton.addActionListener(actionListener);
		weeklyPlanSubmitButton.addActionListener(actionListener);
	}
	
	/*
	 * Action listeners
	 */
	public void submitTemperaturePlan(){
		String lowTemperature = this.lowTemperatureField.getText();
		String highTemperature = this.highTemperatureField.getText();
		try{
			temperaturePlan.setTemperaturePlan(Double.valueOf(lowTemperature), Double.valueOf(highTemperature));
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void submitWeeklyPlan(){
		int rowCount = tableMode.getRowCount();
		int columnCount = tableMode.getColumnCount();
	    ArrayList<HashMap<String, String>> weeklyPlanList = new ArrayList<HashMap<String, String>>();
	    String[] weekDays = new DateFormatSymbols().getWeekdays();
	    
	    for(int i=0; i<rowCount; i++){
	    	HashMap<String, String> dayPlanMap = new HashMap<String, String>();
	    	for(int j=0; j<columnCount; j++){
	    		String value = (String) tableMode.getValueAt(i, j);
	    		if(value == null || value.equals(""))
	    			value = "";
	    		String day = weekDays[j+1].toLowerCase();
	    		dayPlanMap.put(day, value);
	    		System.out.println("day: " + day + ":" + value);
	    	}
	    	weeklyPlanList.add(dayPlanMap);
	    }
	    weeklyPlan.updateWeeklyPlan(this.getGroup(), weeklyPlanList);
	}
	
	public void addRow(){
		String[] rowData =new String[7];
		for(int i=0; i<7; i++){
			rowData[i] = "";
		}
		tableMode.addRow(rowData);
	}
	
	public void showWeeklyPlan() {
		tableMode.setRowCount(0);
		String group = this.groupComboBox.getSelectedItem().toString();
		
		String[] weekDays = new DateFormatSymbols().getWeekdays();
		ArrayList<HashMap<String, String>> groupWeeklyPlan = weeklyPlan.getGroupWeeklyPlan(group);
		if(groupWeeklyPlan == null)
			return;
		
		for(HashMap<String, String> dayPlanMap :  groupWeeklyPlan){
			String[] rowData =new String[7];
			for(int i=0; i<7; i++){
				rowData[i] = dayPlanMap.get(weekDays[i+1].toLowerCase());
			}
			tableMode.addRow(rowData);
		}
	}
}
