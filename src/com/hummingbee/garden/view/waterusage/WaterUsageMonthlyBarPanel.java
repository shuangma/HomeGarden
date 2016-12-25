package com.hummingbee.garden.view.waterusage;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;

import com.hummingbee.garden.common.Utils;
import com.hummingbee.garden.model.sprinkler.SprinklerBank;


public class WaterUsageMonthlyBarPanel extends JPanel{
	public static final String TITLE = "Monthly Water Usage";
	public static final String SPRINKLER_CMD = "sprinkler";
	
	private static final int HEIGHT = WaterUsagePanel.HEIGHT - 45;
	private static final int WIDTH = WaterUsagePanel.WIDTH - 10;
	
	private SprinklerBank sprinklerBank = null;
	
	private JComboBox sprinklerIdComoboBox = null;
	CategoryPlot categoryPlot = null;
	
	public WaterUsageMonthlyBarPanel(SprinklerBank sprinklerBank){
		this.sprinklerBank = sprinklerBank;
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		
		JPanel comboBoxPanel = initComboBoxPanel();
		add(comboBoxPanel);
		
		initBarGraph();
	}
	
	private JPanel initComboBoxPanel(){
		JPanel comboBoxPanel = new JPanel();
		comboBoxPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel sprinklerIdLabel = new JLabel("Sprinkler ID:");
		sprinklerIdLabel.setFont(Utils.getFormatFont(Font.BOLD, 12));
		comboBoxPanel.add(sprinklerIdLabel);
		
		ArrayList<String> sprinklerIds = sprinklerBank.getAllSprinklerIds();
		sprinklerIdComoboBox = new JComboBox(sprinklerIds.toArray());
		sprinklerIdComoboBox.setEditable(false);
		sprinklerIdComoboBox.setActionCommand(SPRINKLER_CMD);
		comboBoxPanel.add(sprinklerIdComoboBox);
		sprinklerIdComoboBox.setFont(Utils.getFormatFont(Font.PLAIN, 16));
		return comboBoxPanel;
	}
	
	private void initBarGraph() {
		DefaultCategoryDataset dataSet = createDataset();
		JFreeChart chart = createChart(dataSet);
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT-50));
		add(chartPanel);
	}
	
	public void addSprinklerChooseListener(ActionListener actionListener){
		sprinklerIdComoboBox.addActionListener(actionListener);
	}
	
	public void update() {
		this.categoryPlot.setDataset(createDataset());;
	}
	
	private DefaultCategoryDataset createDataset(){
    	DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
    	String sprinklerId = sprinklerIdComoboBox.getSelectedItem().toString();
    	HashMap<Integer, Double> waterUsageMap = sprinklerBank.getSprinkerMonthlyWaterUsage(sprinklerId);
    	if(waterUsageMap != null){
    		String[] months = new DateFormatSymbols().getMonths();
    		for(Integer month : waterUsageMap.keySet()){
        		double volume = waterUsageMap.get(month);
        		dataSet.setValue(volume, "Volume", months[month]);
        	}
    	}
    	return dataSet;
    }
    
    private JFreeChart createChart(DefaultCategoryDataset dataSet){
    	JFreeChart barChart = ChartFactory.createBarChart3D(
    			TITLE,             
		        "",             
		        "Volume/GAL",             
		        dataSet,            
		        PlotOrientation.VERTICAL,             
		        false, true, false);
    	barChart.setTitle(new TextTitle(TITLE, Utils.getBorderTitleFont(16)));
    	
    	// Apply some styles
    	categoryPlot = (CategoryPlot) barChart.getPlot();
    	
    	// Set Y axis font and range
    	NumberAxis numberAxis = (NumberAxis) categoryPlot.getRangeAxis();
		numberAxis.setTickLabelFont(Utils.getFormatFont(Font.ITALIC | Font.BOLD, 12));
		numberAxis.setLabelFont(Utils.getFormatFont(Font.ITALIC | Font.BOLD, 14));
		numberAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		
		// Set X axis font
		CategoryAxis categoryAxis = categoryPlot.getDomainAxis();
		categoryAxis.setTickLabelFont(Utils.getFormatFont(Font.ITALIC | Font.BOLD, 12));
		
    	return barChart;
    }
    
    public JComboBox getSprinklerIdComobBox(){
    	return sprinklerIdComoboBox;
    }

}
