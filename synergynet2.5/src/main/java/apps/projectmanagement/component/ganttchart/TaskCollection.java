package apps.projectmanagement.component.ganttchart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TaskCollection {

	protected List<TaskRow> taskRows = new ArrayList<TaskRow>();
	
	public List<TaskRow> getTaskRows(){
		return taskRows;
	}
	
	public void addTaskRow(TaskRow taskRow){
		taskRows.add(taskRow);
		taskRow.setRowIndex(taskRows.size()-1);
		
		if (taskRows.size()<2)
			taskRow.setStartDay(0);
		else {
			int endDayOfLastRow =taskRows.get(taskRows.size()-2).getStartDay()+ taskRows.get(taskRows.size()-2).getDays();
			taskRow.setStartDay(endDayOfLastRow);
		}
		
	}
	
	public void removeTaskRow(TaskRow taskRow){
		taskRow.clear();
		taskRows.remove(taskRow);
		updateRowIndex();
	}
	
	public void swapTaskRow(int row1, int row2){
		Collections.swap(taskRows, row1, row2);
		updateRowIndex();
	}
	
	protected void updateRowIndex(){
		for (int i=0; i<taskRows.size(); i++){
			taskRows.get(i).setRowIndex(i);
		}
		
		
	}
	
}
