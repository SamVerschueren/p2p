package ui.components;

import java.awt.Color;
import java.awt.Component;
import java.text.DecimalFormat;

import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class MyTransferCellRenderer extends DefaultTableCellRenderer {

	private DecimalFormat df = new DecimalFormat("#.##");
	
	public Component getTableCellRendererComponent (JTable table, 
			 Object obj, boolean isSelected, boolean hasFocus, int row, int column) {
		
		Component renderComponent = super.getTableCellRendererComponent(table, obj, isSelected, hasFocus, row, column); 
		
		if(obj == null) {
			return renderComponent;
		}
		 
		 String columnName = table.getColumnName(column);
		 
		 if(columnName.equalsIgnoreCase("progress")) {
			 int value = (Integer)obj;
			 
			 JProgressBar progressBar = new JProgressBar();
			 progressBar.setValue(value);
			 progressBar.setStringPainted(true);
			 //progressBar.setForeground(Color.ORANGE);
			 
			 return progressBar;
		 }
		 else if(columnName.equalsIgnoreCase("filesize")) {
			 String str = "B";
			 long value = (Long)obj;
			 
			 if(value > 1024) {
				 str = "kB";
				 value = value/1024;
			 }
			 if(value > 1024) {
				 str = "MB";
				 value = value/1024;
			 }
			 if(value > 1024) {
				 str = "GB";
				 value = value/1024;
			 }
			 
			 ((JLabel)renderComponent).setText(df.format(value) + " " + str);
		 }
		 else if(columnName.equalsIgnoreCase("speed")) {
			 String str = "B/s";
			 long value = (Long)obj;
			 
			 if(value > 1024) {
				 str = "kB/s";
				 value = value/1024;
			 }
			 if(value > 1024) {
				 str = "MB/s";
				 value = value/1024;
			 }
			 if(value > 1024) {
				 str = "GB/s";
				 value = value/1024;
			 }
			 
			 ((JLabel)renderComponent).setText(df.format(value) + " " + str);
		 }
		 else if(columnName.equalsIgnoreCase("eta")) {
			 int value = (Integer)obj;
			 
			 ((JLabel)renderComponent).setText(value + " s");
		 }
		 
		 return renderComponent;
	}
}
