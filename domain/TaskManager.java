package domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

/**
 * This manager manages the tasks that can happen on a file.
 * 
 * @author Sam Verschueren		<sam.verschueren@gmail.com>
 * @since 6/12/2012
 */
public class TaskManager extends java.util.Observable implements java.util.Observer {

	private Map<String, FileTask> tasks = new HashMap<String, FileTask>();

	public void addTask(FileTask task) {
		this.tasks.put(task.getMd5(), task);
		
		this.setChanged();
		this.notifyObservers("add");
	}
	
	private void removeTask(FileTask task) {
		this.tasks.remove(task.getMd5());
		this.setChanged();
		this.notifyObservers("remove");
	}

	public List<FileTask> getTasks() {
		return new ArrayList<FileTask>(this.tasks.values());
	}
	
	@Override
	public void update(Observable arg0, Object object) {
		if(object instanceof FileTask) {
			FileTask task = (FileTask)object;
			
			if(task.isDone()) {
				this.removeTask(task);
			}
			else if(!this.tasks.containsKey(task.getMd5())) {
				this.addTask(task);
			}
			else {
				this.setChanged();
				this.notifyObservers();
			}
		}
	}
}
