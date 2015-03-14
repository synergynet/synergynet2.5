package apps.projectmanagement.component.staffnode;

import java.util.List;

public class StaffModel {
	private String name;
	private String position;
	private String workExperience;
	private String salary;
	private List<String> notes;
	
	public StaffModel(String name, String position, String workExperience,
			String salary, List<String> notes) {
		super();
		this.name = name;
		this.position = position;
		this.workExperience = workExperience;
		this.salary = salary;
		this.notes = notes;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getWorkExperience() {
		return workExperience;
	}

	public void setWorkExperience(String workExperience) {
		this.workExperience = workExperience;
	}

	public String getSalary() {
		return salary;
	}

	public void setSalary(String salary) {
		this.salary = salary;
	}

	public List<String> getNotes() {
		return notes;
	}

	public void setNotes(List<String> notes) {
		this.notes = notes;
	}
	
	

}
