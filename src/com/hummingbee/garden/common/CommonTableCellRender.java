package com.hummingbee.garden.common;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class CommonTableCellRender extends  DefaultTableCellRenderer{
        public Component getTableCellRendererComponent(JTable table, Object value,  
                    boolean isSelected, boolean hasFocus, int row, int column)   
        {  
            if (row % 2 == 0)  
                setBackground(new Color(160,160,160));  
            else  
                setBackground(new Color(200, 200, 200));  
     
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);  
        }  
}
