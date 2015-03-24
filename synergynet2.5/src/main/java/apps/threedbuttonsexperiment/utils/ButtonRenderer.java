package apps.threedbuttonsexperiment.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import com.jme.scene.Spatial;

import synergynetframework.appsystem.contentsystem.items.Frame;
import synergynetframework.jme.gfx.twod.utils.GraphicsImageQuad;


/**
 * The Class ButtonRenderer.
 */
public class ButtonRenderer {
	
	/**
	 * Render button.
	 *
	 * @param button the button
	 * @param text the text
	 * @param buttonWidth the button width
	 * @param pressed the pressed
	 */
	public static void RenderButton(Frame button, String text, int buttonWidth, boolean pressed){
		
		button.setBorderSize(0);
		button.setBackgroundColour(new Color(54, 55, 56));
		Graphics2D gfx = button.getGraphicsContext();
		
		int buttonLength = buttonWidth;
		
		if (text.equals("s")){
			buttonLength = (int) (buttonWidth*2+6);
		}
		
		if (pressed){
			gfx.setColor(Color.black);
			gfx.drawLine(1, 1, (int) buttonLength, 1);
			gfx.drawLine(1, 1, 1, (int) buttonWidth);
			
			gfx.setColor(Color.gray);
			gfx.drawLine(2, 2, (int) (buttonLength-3), 2);
			gfx.drawLine(2, 2, 2, (int) (buttonWidth-3));
			
			gfx.setColor(Color.white);
			gfx.drawLine(buttonLength-2, 1, buttonLength-2, buttonWidth-2);
			gfx.drawLine(1, buttonWidth-2, buttonLength-2, buttonWidth-2);
			
			gfx.setColor(Color.gray);
			gfx.drawLine(buttonLength-1, 2, buttonLength-1, buttonWidth-1);
			gfx.drawLine(2, buttonWidth-1, buttonLength-1, buttonWidth-1);
		}
		else {
			gfx.setColor(Color.white);
			gfx.drawLine(1, 1, buttonLength-3, 1);
			gfx.drawLine(1, 1, 1, buttonWidth-3);
		
			gfx.setColor(Color.gray);
			gfx.drawLine(2, 2, buttonLength-3, 2);
			gfx.drawLine(2, 2, 2, buttonWidth-3);
		
			gfx.setColor(Color.black);
			gfx.drawLine(buttonLength, 1, buttonLength, buttonWidth);
			gfx.drawLine(1, buttonWidth, buttonLength, buttonWidth);
		
			gfx.setColor(Color.gray);
			gfx.drawLine(buttonLength-1, 2, buttonLength-1, buttonWidth-1);
			gfx.drawLine(2, buttonWidth-1, buttonLength-1, buttonWidth-1);
		}
		
		
		Font font = new Font("Arial Narrow", Font.PLAIN,45);
		gfx.setFont(font);
		gfx.setColor(Color.white);
		
		if (text.equals(".")){
			gfx.fillOval(18, 18, 8, 8);
		}
		else if (text.equals("+")){
			gfx.fillRect(12, 20, 20, 3);
			gfx.fillRect(20, 12, 3, 20);
		}
		else if (text.equals("-")){
			gfx.fillRect(10, 20, 25, 3);
		
		}
		else if (text.equals("d")){
			gfx.fillRect(10, 20, 28, 3);
			
			gfx.drawLine(9, 20, 14, 28);
			gfx.drawLine(10, 20, 15, 28);
			
			gfx.drawLine(10, 20, 15, 12);
			gfx.drawLine(11, 20, 16, 12);
		
		}
		else if (text.equals("s")){
			font = new Font("Arial Narrow", Font.PLAIN,35);
			gfx.setFont(font);
			gfx.drawString("OK", 25, 35);
		}
		else
			gfx.drawString(text, 12, 38);
		
		
		((Spatial)(button.getImplementationObject())).updateGeometricState(0f, false);
		((GraphicsImageQuad)(button.getImplementationObject())).updateGraphics();
	}
	
	
	
}
