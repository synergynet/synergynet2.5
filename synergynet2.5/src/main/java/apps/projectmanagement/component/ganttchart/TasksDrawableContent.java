package apps.projectmanagement.component.ganttchart;

import java.awt.Color;

import com.jmex.awt.swingui.ImageGraphics;

import synergynetframework.appsystem.contentsystem.items.utils.IDrawableContent;


/**
 * The Class TasksDrawableContent.
 */
public class TasksDrawableContent implements IDrawableContent{

	/** The rows. */
	int rows=20;
	
	/** The length. */
	int length = 70*15;
	
	/** The height. */
	int height = 30;
	
	
	/**
	 * Instantiates a new tasks drawable content.
	 *
	 * @param rows the rows
	 * @param length the length
	 * @param height the height
	 */
	public TasksDrawableContent(int rows, int length, int height) {
		this.rows = rows;
		this.length = length;
		this.height = height;
	}

	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.utils.IDrawableContent#drawContent(java.lang.Object)
	 */
	@Override
	public void drawContent(Object drawingHandle) {
		
		ImageGraphics gfx = (ImageGraphics) drawingHandle;
		
		gfx.setColor(Color.WHITE);
		gfx.fillRect(0, 0, length, height*rows);
		
		gfx.setColor(Color.BLACK);
		
		for (int i=0; i<rows; i++){
			
			gfx.drawLine(0, 0+i*height-1, length, 0+i*height-1);
			
		}
		
	}

}
