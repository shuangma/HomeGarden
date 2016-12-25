package com.hummingbee.garden.view.sprinkler;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import com.hummingbee.garden.common.Utils;
import com.hummingbee.garden.controller.SprinklerLabelListener;
import com.hummingbee.garden.model.sprinkler.Sprinkler;
import com.hummingbee.garden.model.sprinkler.SprinklerBank;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

public class SprinklerMapPanel extends JPanel implements Observer{
	private static final int WIDTH = Utils.FRAME_WIDTH/2;
	private static final int HEIGHT = Utils.FRAME_HEIGHT;
	
	private static final String TITLE = "Sprinkler Map";
	
	private HashMap<String, ArrayList<SprinklerLabel>> groupSrpinklerLabelMap = new HashMap<String, ArrayList<SprinklerLabel>>();
	private HashMap<String, SprinklerLabel> idSprinklerMap = new HashMap<String, SprinklerLabel>();
	
	public SprinklerMapPanel(Collection<Sprinkler> sprinklers, SprinklerLabelListener sprinklerLabelListener) {
		Utils.initMainPanelBorder(this, TITLE);
		setPreferredSize(new Dimension(Utils.FRAME_WIDTH/2, Utils.FRAME_HEIGHT));

		setLayout(null);
		
		for(Sprinkler sprinkler : sprinklers){
			SprinklerLabel sprinklerLabel = new SprinklerLabel(sprinkler.getSprinklerGroup(), sprinkler.getSprinklerId(),
					sprinkler.getXLocation(), sprinkler.getYLocation(), sprinkler.isSprinklerOn());
			sprinklerLabel.addLabelMouseListener(sprinklerLabelListener);
			add(sprinklerLabel);
			
			if(!groupSrpinklerLabelMap.containsKey(sprinkler.getSprinklerGroup())){
				ArrayList<SprinklerLabel> sprinklerLabels = new ArrayList<SprinklerLabel>();
				groupSrpinklerLabelMap.put(sprinkler.getSprinklerGroup(), sprinklerLabels);
			}
			ArrayList<SprinklerLabel> sprinklerLabels = groupSrpinklerLabelMap.get(sprinkler.getSprinklerGroup());
			sprinklerLabels.add(sprinklerLabel);
			groupSrpinklerLabelMap.put(sprinkler.getSprinklerGroup(), sprinklerLabels);
			
			idSprinklerMap.put(sprinkler.getSprinklerId(), sprinklerLabel);
		}
	}
	
	@Override
	protected void paintComponent(Graphics graphics) {
	    super.paintComponent(graphics);
	    try {
			Image image = ImageIO.read(new File(Utils.GARDEN_MAP_IMG));
			Image scaledImage = image.getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH);
			graphics.drawImage(scaledImage, 0, 0, this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void update(Observable observable, Object object) {
		String observableType = (String) object;
		if(observableType.equals(SprinklerBank.SPRINKLER_BANK_OBSERVABLE)){
			SprinklerBank sprinklerBank = (SprinklerBank) observable;
			for(String sprinklerId : idSprinklerMap.keySet()){
				boolean isSprinklerOn = sprinklerBank.isSprinklerTurnedOn(sprinklerId);
				SprinklerLabel sprinklerLabel = idSprinklerMap.get(sprinklerId);
				if(sprinklerLabel == null)
					continue;
				if(isSprinklerOn){
					sprinklerLabel.onSpriklerOn();
				}else{
					sprinklerLabel.onSprinklerOff();
				}
			}
		}else if(observableType.equals(Sprinkler.SPRINKLER_OBSERVABLE)){
			Sprinkler sprinkler = (Sprinkler) observable;
			SprinklerLabel sprinklerLabel = idSprinklerMap.get(sprinkler.getSprinklerId());
			if(sprinklerLabel == null)
				return;
			if(sprinkler.isSprinklerOn())
				sprinklerLabel.onSpriklerOn();
			else
				sprinklerLabel.onSprinklerOff();
				
		}
	}
	
}
