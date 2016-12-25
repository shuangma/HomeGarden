package com.hummingbee.garden.controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import com.hummingbee.garden.model.sprinkler.SprinklerBank;
import com.hummingbee.garden.view.sprinkler.SprinklerLabel;

public class SprinklerLabelListener implements MouseListener{
	private SprinklerBank sprinklerBank = null;
	
	public SprinklerLabelListener(SprinklerBank sprinklerBank){
		this.sprinklerBank = sprinklerBank;
	}
	
	@Override
	public void mouseClicked(MouseEvent event) {
		SprinklerLabel sprinklerLabel = (SprinklerLabel) event.getSource();
		String sprinklerId = sprinklerLabel.getSprinklerId();
		
		// If previous sprinkler status is on, then turn off
		if(sprinklerLabel.isSprinklerOn()){
			sprinklerBank.turnOffSprinkler(sprinklerId, true);
		}else{
			sprinklerBank.turnOnSprinkler(sprinklerId, true);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
