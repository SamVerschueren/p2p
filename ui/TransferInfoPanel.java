package ui;

import java.awt.BorderLayout;
import java.util.Enumeration;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

import ui.components.MyTransferCellRenderer;

import domain.DomainController;

/**
 * This panel shows information about a file you've downloading.
 * 
 * @author Matts Devriendt 	<matts.devriendt@gmail.com>
 * @author Bart Beyers 		<bart.beyers@gmail.com>
 * @author Sam Verschueren	<sam.verschueren@gmail.com>
 * @since 20/10/2012
 */
public class TransferInfoPanel extends JPanel {
	
    private DomainController controller;
    private JTable table;

    /**
     * Creates a new instance of the panel.
     */
    public TransferInfoPanel(DomainController controller) {
        this.controller = controller;

        this.init();
    }

    /**
     * Initializes the gui components.
     */
    private void init() {
    	setLayout(new BorderLayout());
    	
    	MyTransferCellRenderer renderer = new MyTransferCellRenderer();
    	
    	table = new JTable(controller.getTransferInfoTableModel());
    	Enumeration<TableColumn> columns = table.getColumnModel().getColumns();
    	
    	while(columns.hasMoreElements()) {
    		TableColumn column = columns.nextElement();
    		
    		column.setCellRenderer(renderer);
    	}
    	
    	add(new JScrollPane(table), BorderLayout.CENTER);
    }
}
