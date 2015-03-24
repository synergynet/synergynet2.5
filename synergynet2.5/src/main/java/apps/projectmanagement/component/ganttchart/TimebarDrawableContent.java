package apps.projectmanagement.component.ganttchart;

import java.awt.Color;
import java.awt.Font;

import synergynetframework.appsystem.contentsystem.items.utils.IDrawableContent;

import com.jmex.awt.swingui.ImageGraphics;

/**
 * The Class TimebarDrawableContent.
 */
public class TimebarDrawableContent implements IDrawableContent {
	
	/** The height. */
	int height = 20;

	/** The inner line lenght. */
	int innerLineLenght = 8;

	/** The length. */
	int length = 70;

	/** The repeats. */
	int repeats = 0;

	/** The title. */
	String title = "Week";

	/**
	 * Instantiates a new timebar drawable content.
	 *
	 * @param repeats
	 *            the repeats
	 * @param title
	 *            the title
	 * @param length
	 *            the length
	 * @param height
	 *            the height
	 */
	public TimebarDrawableContent(int repeats, String title, int length,
			int height) {
		this.repeats = repeats;
		this.title = title;
		this.length = length;
		this.height = height;
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

		gfx.setColor(Color.BLACK);
		
		String timebarTitle = "";
		Font font = new Font("Arial", Font.PLAIN, 12);
		gfx.setFont(font);

		for (int i = 0; i < repeats; i++) {

			timebarTitle = title + " " + (i + 1);

			gfx.drawLine(0 + (length * i), 0, 0 + (length * i), height);
			gfx.drawLine(0 + (length * i), height / 2, 0 + (length * i)
					+ innerLineLenght, height / 2);
			gfx.drawString(timebarTitle, (length * i) + innerLineLenght + 3,
					(height / 2) + 5);
			gfx.drawLine(length + (length * i), 0, length + (length * i),
					height);
			gfx.drawLine(length + (length * i), height / 2,
					(length + (length * i)) - innerLineLenght, height / 2);
		}

	}
	
}
