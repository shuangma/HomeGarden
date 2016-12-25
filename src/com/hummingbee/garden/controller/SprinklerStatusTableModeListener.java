package com.hummingbee.garden.controller;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import com.hummingbee.garden.model.sprinkler.SprinklerBank;

public class SprinklerStatusTableModeListener implements TableModelListener {

	private SprinklerBank sprinklerBank = null;
	
	public SprinklerStatusTableModeListener(SprinklerBank sprinklerBank){
		this.sprinklerBank = sprinklerBank;
	}
	
	@Override
	public void tableChanged(TableModelEvent event) {
		int row = event.getFirstRow();
	    int column = event.getColumn();
	    TableModel tableModel = (TableModel) event.getSource();
	    String columnName = tableModel.getColumnName(column);
	    if(columnName != null && columnName.toLowerCase().equals("waterflux") && column != -1){
	    	String waterFlux = (String) tableModel.getValueAt(row, column);
	    	String sprinklerId = (String) tableModel.getValueAt(row, 1);
	    	if(waterFlux != null && sprinklerId != null){
	    		try{
	    			updateWaterFlux(sprinklerId.trim(), Double.valueOf(waterFlux));
	    		}catch(Exception ex){
	    			 
	    		}
	    	}
	    }
	}
	
	private void updateWaterFlux(String sprinklerId, double waterFlux){
		sprinklerBank.updateSprinklerWaterFlux(sprinklerId, waterFlux);
	}

}
