package apps.projectmanagement.component.staffnode;

import java.awt.Color;
import java.awt.Font;
import java.util.List;

import synergynetframework.appsystem.contentsystem.items.utils.IDrawableContent;

import com.jmex.awt.swingui.ImageGraphics;

/**
 * The Class StaffNodeDrawableContent.
 */
public class StaffNodeDrawableContent implements IDrawableContent {
	
	/** The heigth. */
	int heigth = 150;

	/** The name. */
	String name;

	/** The notes. */
	List<String> notes;

	/** The position. */
	String position;

	/** The salary. */
	String salary;

	/** The width. */
	int width = 150;

	/** The work experience. */
	String workExperience;

	/**
	 * Instantiates a new staff node drawable content.
	 *
	 * @param staffModel
	 *            the staff model
	 * @param width
	 *            the width
	 * @param height
	 *            the height
	 */
	public StaffNodeDrawableContent(StaffModel staffModel, int width, int height) {
		super();
		this.name = staffModel.getName();
		this.position = staffModel.getPosition();
		this.workExperience = staffModel.getWorkExperience();
		this.salary = staffModel.getSalary();
		this.notes = staffModel.getNotes();
		this.width = width;
		this.heigth = height;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.utils.IDrawableContent
	 * #drawContent(java.lang.Object)
	 */
	@Override
	public void drawContent(Object drawingHandle) {

		ImageGraphics gfx = (ImageGraphics) drawingHandle;

		gfx.setColor(Color.WHITE);
		gfx.fillRect(0, 0, width, heigth);

		gfx.setColor(Color.gray);
		Font font = new Font("Arial", Font.BOLD, 10);
		gfx.setFont(font);

		int rowHeigth = 15;

		gfx.drawString("Name:", 5, rowHeigth * 2);
		gfx.drawString("Position:", 5, rowHeigth * 3);
		gfx.drawString("Work Exp.:", 5, rowHeigth * 4);
		gfx.drawString("Salary:", 5, rowHeigth * 5);
		gfx.drawString("Notes:", 5, rowHeigth * 6);

		gfx.setColor(Color.BLACK);
		font = new Font("Arial", Font.PLAIN, 9);
		gfx.setFont(font);

		gfx.drawString(name, 37, rowHeigth * 2);
		gfx.drawString(this.position, 50, rowHeigth * 3);
		gfx.drawString(this.workExperience + " yrs", 62, rowHeigth * 4);
		gfx.drawString("£" + this.salary + " per hour", 42, rowHeigth * 5);

		if (notes == null) {
			return;
		}

		for (int i = 0; i < notes.size(); i++) {

			gfx.drawString(notes.get(i), 5, rowHeigth * (7 + i));
		}
		
	}
	
}
