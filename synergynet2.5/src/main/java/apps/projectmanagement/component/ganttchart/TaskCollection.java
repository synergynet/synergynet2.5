package apps.projectmanagement.component.ganttchart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The Class TaskCollection.
 */
public class TaskCollection {
	
	/** The task rows. */
	protected List<TaskRow> taskRows = new ArrayList<TaskRow>();

	/**
	 * Adds the task row.
	 *
	 * @param taskRow
	 *            the task row
	 */
	public void addTaskRow(TaskRow taskRow) {
		taskRows.add(taskRow);
		taskRow.setRowIndex(taskRows.size() - 1);

		if (taskRows.size() < 2) {
			taskRow.setStartDay(0);
		} else {
			int endDayOfLastRow = taskRows.get(taskRows.size() - 2)
					.getStartDay()
					+ taskRows.get(taskRows.size() - 2).getDays();
			taskRow.setStartDay(endDayOfLastRow);
		}

	}

	/**
	 * Gets the task rows.
	 *
	 * @return the task rows
	 */
	public List<TaskRow> getTaskRows() {
		return taskRows;
	}

	/**
	 * Removes the task row.
	 *
	 * @param taskRow
	 *            the task row
	 */
	public void removeTaskRow(TaskRow taskRow) {
		taskRow.clear();
		taskRows.remove(taskRow);
		updateRowIndex();
	}

	/**
	 * Swap task row.
	 *
	 * @param row1
	 *            the row1
	 * @param row2
	 *            the row2
	 */
	public void swapTaskRow(int row1, int row2) {
		Collections.swap(taskRows, row1, row2);
		updateRowIndex();
	}

	/**
	 * Update row index.
	 */
	protected void updateRowIndex() {
		for (int i = 0; i < taskRows.size(); i++) {
			taskRows.get(i).setRowIndex(i);
		}
		
	}

}
