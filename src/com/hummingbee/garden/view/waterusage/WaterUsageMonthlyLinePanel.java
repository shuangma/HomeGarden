package com.hummingbee.garden.view.waterusage;

import java.awt.Dimension;
import java.awt.Font;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;

import com.hummingbee.garden.common.Utils;
import com.hummingbee.garden.model.sprinkler.SprinklerBank;

public class WaterUsageMonthlyLinePanel extends JPanel{
	public static final String TITLE = "Water Usage Trend";
	
	private static final int HEIGHT = WaterUsagePanel.HEIGHT - 45;
	private static final int WIDTH = WaterUsagePanel.WIDTH - 10;
	
	private SprinklerBank sprinklerBank = null;
	
	CategoryPlot categoryPlot = null;
	
	public WaterUsageMonthlyLinePanel(SprinklerBank sprinklerBank){
		this.sprinklerBank = sprinklerBank;
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		
		initLineGraph();
	}
	
	private void initLineGraph() {
		CategoryDataset dataSet = createDataset();
		JFreeChart chart = createChart(dataSet);
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT-50));
		add(chartPanel);
	}
	
	public void update() {
		this.categoryPlot.setDataset(createDataset());
	}
	
	private CategoryDataset createDataset(){
		ArrayList<String> sprinklerIds = sprinklerBank.getAllSprinklerIds();
		String[] rowKeys = new String[sprinklerIds.size()];
		int sprinklerCount = 0;
		for(String sprinklerId : sprinklerIds){
			rowKeys[sprinklerCount] = sprinklerId;
			sprinklerCount ++;
		}
		
        String[] columnKeys = new String[12];
        int monthCount = 0;
        for(String month : new DateFormatSymbols().getShortMonths()){
        	if(month != null && !month.equals("")){
        		columnKeys[monthCount] = month;
        		monthCount ++;
        	}
   
        }
        double[][] data = new double[sprinklerIds.size()][12];
        
        sprinklerCount = 0;
        
        for(String sprinklerId : sprinklerIds){
	    	HashMap<Integer, Double> waterUsageMap = sprinklerBank.getSprinkerMonthlyWaterUsage(sprinklerId);
	    	if(waterUsageMap != null){
	    		for(int month=0; month < 12; month++){
	    			double volume = 0.0;
	    			if(waterUsageMap.containsKey(month))
	    				volume = waterUsageMap.get(month);
	    			data[sprinklerCount][month] = volume;
	    		}
	    	}
	    	sprinklerCount ++;
        }
        
        return DatasetUtilities.createCategoryDataset(rowKeys, columnKeys, data);
    }
    
    private JFreeChart createChart(CategoryDataset dataSet){
    	String chartTitle = "Water Usage Trend";
    	
    	JFreeChart lineChart = ChartFactory.createLineChart(
    			TITLE,             
		        "",             
		        "Volume/GAL",             
		        dataSet,            
		        PlotOrientation.VERTICAL,             
		        true, true, false);
    	lineChart.setTitle(new TextTitle(TITLE, Utils.getBorderTitleFont(16)));
    	lineChart.getLegend().setItemFont(Utils.getFormatFont(Font.ITALIC, 14));
    	
    	// Apply some styles
    	categoryPlot = (CategoryPlot) lineChart.getPlot();
    	
    	 LineAndShapeRenderer renderer = (LineAndShapeRenderer) categoryPlot.getRenderer();  
         renderer.setBaseLinesVisible(true); 
         renderer.setUseSeriesOffset(true); 
    	
    	// Set Y axis font and range
    	NumberAxis numberAxis = (NumberAxis) categoryPlot.getRangeAxis();
		numberAxis.setTickLabelFont(Utils.getFormatFont(Font.ITALIC | Font.BOLD, 12));
		numberAxis.setLabelFont(Utils.getFormatFont(Font.ITALIC | Font.BOLD, 14));
		numberAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		
		// Set X axis font
		CategoryAxis categoryAxis = categoryPlot.getDomainAxis();
		categoryAxis.setTickLabelFont(Utils.getFormatFont(Font.ITALIC | Font.BOLD, 12));
		
    	return lineChart;
    }
}
