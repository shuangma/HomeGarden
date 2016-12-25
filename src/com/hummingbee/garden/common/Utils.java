package com.hummingbee.garden.common;

import java.awt.Component;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.border.TitledBorder;

public class Utils {
	// Constants
	public static final int FRAME_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width-200;
	public static final int FRAME_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height-100;
	
	public static final String SPRINKLER_TABLE = "sprinkler";
	public static final String WEEKLY_PLAN_TABLE = "weekly_plan";
	public static final String WATER_USAGE_TABLE = "water_usage";
	
	public static final String PICATURE_DIR = new File("resources", "pictures").getPath();
	public static final String SPRINKLER_ON_ICON = new File(PICATURE_DIR, "sprinklerOn.png").getPath();
	public static final String SPRINKLER_OFF_ICON = new File(PICATURE_DIR, "sprinklerOff.png").getPath();
	public static final String GARDEN_MAP_IMG = new File(PICATURE_DIR, "gardenmap.jpg").getPath();
	
	public static String formatOnlyDate(Date date){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(date);
	}
	
	public static int getMonthFromString(String dateStr){
		if(dateStr == null || dateStr.equals(""))
			return -1;
		
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try{
			date = format.parse(dateStr);
		}catch(Exception ex){
			ex.printStackTrace();
			return -1;
		}
		
        Calendar calendar = Calendar.getInstance();  
        calendar.setTime(date);  
        return calendar.get(Calendar.MONTH);  
	}
	
	public static String getDayOfWeekFromString(String dateStr){
		if(dateStr == null || dateStr.equals(""))
			return null;
		
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try{
			date = format.parse(dateStr);
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
		
        Calendar calendar = Calendar.getInstance();  
        calendar.setTime(date);  
        int weekOfDayIndex = calendar.get(Calendar.MONTH);  
        if(weekOfDayIndex < 0){  
        	weekOfDayIndex = 0;  
        }   
        return new DateFormatSymbols().getWeekdays()[weekOfDayIndex];
	}
	
	public static String getDayOfWeekFromDate(Date date){
        Calendar calendar = Calendar.getInstance();  
        calendar.setTime(date);  
        int weekOfDayIndex = calendar.get(Calendar.DAY_OF_WEEK);  
        if(weekOfDayIndex < 0){  
        	weekOfDayIndex = 0;  
        }   
        return new DateFormatSymbols().getWeekdays()[weekOfDayIndex]; 
	}
	
	public static int getYearFromDate(Date date){
		Calendar calendar = Calendar.getInstance();  
        calendar.setTime(date);  
        int year = calendar.get(Calendar.YEAR);
        return year;
	}

	public static int getMonthFromDate(Date date){
		Calendar calendar = Calendar.getInstance();  
        calendar.setTime(date);  
        int month = calendar.get(Calendar.MONTH)+1;
        return month;
	}

	public static int getDayFromDate(Date date){
		Calendar calendar = Calendar.getInstance();  
        calendar.setTime(date);  
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return day;
	}

	public static int getHourFromDate(Date date){
		Calendar calendar = Calendar.getInstance();  
        calendar.setTime(date);  
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        return hour;
	}
	
	public static int getMinuteFromDate(Date date){
		Calendar calendar = Calendar.getInstance();  
        calendar.setTime(date);  
        int minute = calendar.get(Calendar.MINUTE);
        return minute;
	}
	
	public static String join(ArrayList<String> input, String delimiter){
		String output = "";
		int length = input.size();
		for(int i=0; i < length; i++){
			if(i == length -1)
				output += input.get(i);
			else
				output += (input.get(i) + delimiter);
		}
		
		return output;
	}
	
	public static void initMainPanelBorder(JComponent panel, String title){
		TitledBorder border = new TitledBorder(title);
		border.setTitleFont(Utils.getBorderTitleFont(25));
		panel.setBorder(border);
	}
	
	public static void initSubPanelBorder(JComponent panel, String title){
		TitledBorder border = new TitledBorder(title);
		border.setTitleFont(Utils.getBorderTitleFont(18));
		panel.setBorder(border);
	}
	
	public static Font getBorderTitleFont(int fontSize){
		Font font = new Font("Times New Roman", Font.BOLD | Font.ITALIC, fontSize);
		return font;
	}
	
	public static Font getFormatFont(){
		Font font = new Font("Times New Roman", Font.PLAIN, 16);
		return font;
	}
	
	public static Font getFormatFont(int mode, int size){
		Font font = new Font("Times New Roman", Font.PLAIN | mode, size);
		return font;
	}

	public static void showInfoMessageDialog(Component parentComponent, String message){
		JLabel label = new JLabel(message);
		label.setFont(Utils.getFormatFont(Font.PLAIN, 16));
		JOptionPane.showMessageDialog(parentComponent, label);
	}
	
	public static String readFile(String filePath){
		StringBuilder content = new StringBuilder();
		File inputFile = new File(filePath);
		if(inputFile.exists()){
			BufferedReader bufferedReader = null;
			try {
				bufferedReader = new BufferedReader(new FileReader(filePath));
		        String line = bufferedReader.readLine();

		        while (line != null) {
		        	content.append(line);
		            line = bufferedReader.readLine();
		        }
			} catch (Exception e) {
				e.printStackTrace();
		    } finally {
		    	if(bufferedReader != null)
					try {
						bufferedReader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
		    }
		}
		return content.toString();
	}
}
