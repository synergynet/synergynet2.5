package apps.projectmanagement.component.staffnode;

import java.util.List;

/**
 * The Class StaffModel.
 */
public class StaffModel {

	/** The name. */
	private String name;

	/** The notes. */
	private List<String> notes;

	/** The position. */
	private String position;

	/** The salary. */
	private String salary;

	/** The work experience. */
	private String workExperience;

	/**
	 * Instantiates a new staff model.
	 *
	 * @param name
	 *            the name
	 * @param position
	 *            the position
	 * @param workExperience
	 *            the work experience
	 * @param salary
	 *            the salary
	 * @param notes
	 *            the notes
	 */
	public StaffModel(String name, String position, String workExperience,
			String salary, List<String> notes) {
		super();
		this.name = name;
		this.position = position;
		this.workExperience = workExperience;
		this.salary = salary;
		this.notes = notes;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the notes.
	 *
	 * @return the notes
	 */
	public List<String> getNotes() {
		return notes;
	}
	
	/**
	 * Gets the position.
	 *
	 * @return the position
	 */
	public String getPosition() {
		return position;
	}
	
	/**
	 * Gets the salary.
	 *
	 * @return the salary
	 */
	public String getSalary() {
		return salary;
	}
	
	/**
	 * Gets the work experience.
	 *
	 * @return the work experience
	 */
	public String getWorkExperience() {
		return workExperience;
	}
	
	/**
	 * Sets the name.
	 *
	 * @param name
	 *            the new name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Sets the notes.
	 *
	 * @param notes
	 *            the new notes
	 */
	public void setNotes(List<String> notes) {
		this.notes = notes;
	}
	
	/**
	 * Sets the position.
	 *
	 * @param position
	 *            the new position
	 */
	public void setPosition(String position) {
		this.position = position;
	}
	
	/**
	 * Sets the salary.
	 *
	 * @param salary
	 *            the new salary
	 */
	public void setSalary(String salary) {
		this.salary = salary;
	}
	
	/**
	 * Sets the work experience.
	 *
	 * @param workExperience
	 *            the new work experience
	 */
	public void setWorkExperience(String workExperience) {
		this.workExperience = workExperience;
	}
	
}
