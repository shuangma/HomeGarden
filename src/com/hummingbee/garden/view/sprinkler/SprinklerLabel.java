package com.hummingbee.garden.view.sprinkler;

import java.awt.Font;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import com.hummingbee.garden.common.Utils;
import com.hummingbee.garden.common.ViewHelper;

public class SprinklerLabel extends JLabel {
	private static final int LABEL_WIDTH = 65;
	private static final int LABEL_HEIGHT = 65;
	
	private ImageIcon sprinklerOffIcon = ViewHelper.getResizeImageIcon(LABEL_HEIGHT-35, LABEL_WIDTH-35, Utils.SPRINKLER_OFF_ICON);
	private ImageIcon sprinklerOnIcon = ViewHelper.getResizeImageIcon(LABEL_HEIGHT-35, LABEL_WIDTH-35, Utils.SPRINKLER_ON_ICON);
	
	private boolean isSprinklerOn = false;
	
	private String sprinklerId;
	private String sprinklerGroup;
	
	public SprinklerLabel(String sprinklerGroup, String sprinklerId, int xLocation, int yLocation, boolean isOn){
		super();
		
		this.sprinklerGroup = sprinklerGroup;
		this.sprinklerId = sprinklerId;
		
		this.setText(sprinklerId.toUpperCase());
		this.setFont(Utils.getFormatFont(Font.BOLD, 12));
		this.setIcon(isOn ? sprinklerOnIcon : sprinklerOffIcon);
		this.setSize(LABEL_HEIGHT, LABEL_WIDTH);
		this.setLocation(xLocation, yLocation);
	}
	
	public void addLabelMouseListener(MouseListener mounseListener){
		this.addMouseListener(mounseListener);
	}
	
	public String getSprinklerGroup() {
		return sprinklerGroup;
	}
	
	public String getSprinklerId(){
		return sprinklerId;
	}
	
	public void onSprinklerOff(){
		setIcon(sprinklerOffIcon);
		isSprinklerOn = false;
	}
	
	public void onSpriklerOn(){
		setIcon(sprinklerOnIcon);
		isSprinklerOn = true;
	}
	
	public boolean isSprinklerOn(){
		return isSprinklerOn;
	}
}
