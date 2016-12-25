package com.hummingbee.garden.view.sprinkler;

import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.hummingbee.garden.common.CommonTableCellRender;
import com.hummingbee.garden.common.Utils;
import com.hummingbee.garden.model.sprinkler.Sprinkler;
import com.hummingbee.garden.model.sprinkler.SprinklerBank;
import com.hummingbee.garden.view.operation.OperationPanel;

public class SprinklerStatusPanel extends JPanel implements Observer{
	public static final int WIDTH = Utils.FRAME_WIDTH/2;
	public static final int HEIGHT = Utils.FRAME_HEIGHT/2 - OperationPanel.HEIGHT;
	private static final String TITLE = "Sprinkler Status";
	private static final String[] TABLE_HEADERS = {"Group", "Sprinkler ID", "isON", "isFunctional", "WaterFlux"};
	
	private static Object[][] cellData = null;
	static DefaultTableModel tableMode = new DefaultTableModel(cellData, TABLE_HEADERS) {
	  public boolean isCellEditable(int row, int column) {
		  if(column == 4)
			  return true;
		  else
			  return false;
	  }
	};
	
	JTable statusTable = new JTable(tableMode);

	public SprinklerStatusPanel(SprinklerBank sprinklerBank){
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		Utils.initMainPanelBorder(this, TITLE);
		
		add(createTablePanel());
		
		this.fillTable(sprinklerBank);
	}

	private JScrollPane createTablePanel(){
		JScrollPane scrollPane = new JScrollPane(statusTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		setTableStyle();
		return scrollPane;
	}
	
	private void setTableStyle(){
		statusTable.setFont(Utils.getFormatFont(Font.BOLD | Font.ITALIC, 12));
		statusTable.getTableHeader().setFont(Utils.getFormatFont(Font.BOLD, 16));
		TableColumnModel columnModel = statusTable.getColumnModel();  
        for (int i = 0, n = columnModel.getColumnCount(); i < n; i++)   
        {  
            TableColumn column = columnModel.getColumn(i);  
            column.setCellRenderer(new CommonTableCellRender());  
        }  
	}
	
	public void addTableModeListener(TableModelListener tableModeListener){
		statusTable.getModel().addTableModelListener(tableModeListener);
	}
	
	private void fillTable(SprinklerBank sprinklerBank){
		tableMode.setRowCount(0);
		
		for(String group : sprinklerBank.getSprinklerGroups()){
			ArrayList<Sprinkler> sprinklers = sprinklerBank.getGroupSprinklers(group);
			for(Sprinkler sprinkler : sprinklers){
				String[] rowData =new String[5];
				rowData[0] = sprinkler.getSprinklerGroup();
				rowData[1] = sprinkler.getSprinklerId();
				rowData[2] = sprinkler.isSprinklerOn() ? "YES" : "NO";
				rowData[3] = sprinkler.isSprinklerFunctional() ? "YES" : "NO";
				rowData[4] = String.valueOf(sprinkler.getWaterFlux());
			    
			    tableMode.addRow(rowData);
			}
		}
	}
	
	@Override
	public void update(Observable observable, Object object) {
		String observableType = (String) object;
		if(observableType.equals(SprinklerBank.SPRINKLER_BANK_OBSERVABLE)){
			
			SprinklerBank sprinklerBank = (SprinklerBank) observable;
			fillTable(sprinklerBank);
		}else if(observableType.equals(Sprinkler.SPRINKLER_OBSERVABLE)){
			Sprinkler sprinkler = (Sprinkler) observable;
			for(int row=0; row < tableMode.getRowCount(); row++){
				if(tableMode.getValueAt(row, 0).equals(sprinkler.getSprinklerGroup()) && 
						tableMode.getValueAt(row, 1).equals(sprinkler.getSprinklerId())){
					tableMode.setValueAt(sprinkler.isSprinklerOn() ? "YES" : "NO", row, 2);
					tableMode.setValueAt(sprinkler.isSprinklerFunctional() ? "YES" : "NO", row, 3);
					tableMode.setValueAt(String.valueOf(sprinkler.getWaterFlux()), row, 4);
				}
			}
		}
		statusTable.invalidate();
	}
}

