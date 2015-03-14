package apps.projectmanagement.component.staffnode;

import java.awt.Color;
import java.awt.Font;
import java.util.List;

import com.jmex.awt.swingui.ImageGraphics;

import synergynetframework.appsystem.contentsystem.items.utils.IDrawableContent;

public class StaffNodeDrawableContent implements IDrawableContent{

	String name;
	String position;
	String workExperience;
	String salary;
	List<String> notes;
	int width = 150;
	int heigth = 150;
	
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
	
	@Override
	public void drawContent(Object drawingHandle) {
		
		ImageGraphics gfx = (ImageGraphics) drawingHandle;
		
		gfx.setColor(Color.WHITE);
		gfx.fillRect(0, 0, width, heigth);
		
		gfx.setColor(Color.gray);
		Font font = new Font("Arial", Font.BOLD, 10);
		gfx.setFont(font);
		
		int rowHeigth = 15;
		
		gfx.drawString("Name:", 5, rowHeigth*2);
		gfx.drawString("Position:", 5, rowHeigth*3);
		gfx.drawString("Work Exp.:", 5, rowHeigth*4);
		gfx.drawString("Salary:", 5, rowHeigth*5);
		gfx.drawString("Notes:", 5, rowHeigth*6);
		
		gfx.setColor(Color.BLACK);
		font = new Font("Arial", Font.PLAIN, 9);
		gfx.setFont(font);
		
		gfx.drawString(name, 37, rowHeigth*2);
		gfx.drawString(this.position, 50, rowHeigth*3);
		gfx.drawString(this.workExperience+" yrs", 62, rowHeigth*4);
		gfx.drawString("£"+this.salary+" per hour", 42, rowHeigth*5);
		
		if (notes==null) return;
		
		for (int i=0; i<notes.size(); i++){
			
			gfx.drawString(notes.get(i), 5, rowHeigth*(7+i));
		}
		
		
		
		
	}

}
