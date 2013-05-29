package domain.listmodel;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Observable;
import java.util.Observer;

import javax.swing.table.AbstractTableModel;

import domain.FileTask;
import domain.TaskManager;

/**
 * Creates a <em>TableModel</em>
 * 
 * @author Sam Verschueren		<sam.verschueren@gmail.com>
 * @since 6/12/2012
 */
public class TransferTableModel extends AbstractTableModel implements Observer {

	private static final String COLUMN[] = {"Filename", "Progress", "Status", "Filesize", "Speed", "ETA"};
	private TaskManager taskManager;
	
	public TransferTableModel(TaskManager taskManager) {
		this.taskManager = taskManager;
		this.taskManager.addObserver(this);
	}
	
	@Override
	public int getColumnCount() {
		return COLUMN.length;
	}

	@Override
	public int getRowCount() {
		return this.taskManager.getTasks().size();
	}
	
	@Override
	public String getColumnName(int index) {
		return COLUMN[index];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if(rowIndex > this.taskManager.getTasks().size()-1) {
			return null;
		}
		
		FileTask task = this.taskManager.getTasks().get(rowIndex);
		
		try {
			Method[] methods = task.getClass().getDeclaredMethods();

			for(Method m:methods) {
				if(m.getName().equalsIgnoreCase("get" + COLUMN[columnIndex])) {
					Object value = m.invoke(task, new Object[] { });
					
					return value;
				}
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if(arg1 == null) {
			this.fireTableDataChanged();
		}
		else if(arg1.toString().equals("add")) {
			this.fireTableRowsInserted(this.taskManager.getTasks().size()-1, this.taskManager.getTasks().size()-1);
		}
		else if(arg1.toString().equals("remove")) {
			this.fireTableRowsDeleted(0, this.taskManager.getTasks().size());
		}
	}
}
