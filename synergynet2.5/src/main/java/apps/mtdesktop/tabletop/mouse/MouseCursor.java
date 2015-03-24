package apps.mtdesktop.tabletop.mouse;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import apps.mtdesktop.MTDesktopConfigurations;
import apps.mtdesktop.desktop.DesktopClient;
import apps.mtdesktop.messages.util.MouseEventInfo;
import apps.mtdesktop.tabletop.TabletopContentManager.DesktopMouseListener;

import com.jme.math.FastMath;
import com.jme.math.Vector2f;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.LightImageLabel;
import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.jme.cursorsystem.MultiTouchCursorSystem;
import synergynetframework.mtinput.MultiTouchInputComponent;
import synergynetframework.mtinput.events.MultiTouchCursorEvent;


/**
 * The Class MouseCursor.
 */
public class MouseCursor implements DesktopMouseListener{
	
	/** The cursor. */
	protected LightImageLabel cursor;
	
	/** The table id. */
	protected TableIdentity tableId;
	
	/** The cursor width. */
	private int cursorWidth = 25;
	
	/** The cursor height. */
	private int cursorHeight = 40;
	
	/** The cursor shift x. */
	private int cursorShiftX = -cursorWidth/2;
	
	/** The cursor shift y. */
	private int cursorShiftY = cursorHeight/2;
	
	/** The input. */
	private MultiTouchInputComponent input;
	
	/** The cursor id. */
	private int cursorId;
	
	/**
	 * Instantiates a new mouse cursor.
	 *
	 * @param contentSystem the content system
	 * @param input the input
	 * @param tableId the table id
	 * @param cursorId the cursor id
	 * @param color the color
	 */
	public MouseCursor(ContentSystem contentSystem, MultiTouchInputComponent input, TableIdentity tableId, int cursorId, Color color){
		
        BufferedImage cursorImage = null;
		try {
	        Image img = (new ImageIcon(MTDesktopConfigurations.class.getResource("tabletop/cursor.png"))).getImage(); 
	        cursorImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB); 
	        Graphics g = cursorImage.createGraphics(); 
	        g.drawImage(img, 0, 0, null); 
	        g.dispose();
			cursorImage = ColorChanger.changeColor(cursorImage, Color.white,color);
			File coloredImageFile = new File(MTDesktopConfigurations.tabletopTempFolder +"/"+tableId+".png");
	        ImageIO.write(cursorImage, "png", coloredImageFile);
	        while(!coloredImageFile.exists()){};
			cursor = (LightImageLabel) contentSystem.createContentItem(LightImageLabel.class);
			if(cursorImage != null)
				cursor.drawImage(new File(MTDesktopConfigurations.tabletopTempFolder +"/"+tableId+".png").toURI().toURL());
			else
				cursor.drawImage(MTDesktopConfigurations.class.getResource("tabletop/cursor.png"));
			cursor.setAutoFitSize(false);
			cursor.setWidth(cursorWidth);
			cursor.setHeight(cursorWidth);
			cursor.centerItem();
			cursor.setAsTopObject();
			cursor.setBringToTopable(false);
			this.input = input;
			this.tableId = tableId;
		} catch (IOException e) {
			 
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the cursor.
	 *
	 * @return the cursor
	 */
	public LightImageLabel getCursor(){
		return cursor;
	}
	
	/**
	 * Gets the table identity.
	 *
	 * @return the table identity
	 */
	public TableIdentity getTableIdentity(){
		return tableId;
	}

	/* (non-Javadoc)
	 * @see apps.mtdesktop.tabletop.TabletopContentManager.DesktopMouseListener#mousePressed(synergynetframework.appsystem.services.net.localpresence.TableIdentity, apps.mtdesktop.messages.util.MouseEventInfo)
	 */
	public void mousePressed(TableIdentity tableId, MouseEventInfo evt) {
		if(this.tableId == null || !this.tableId.equals(tableId)) return;
		Point.Float tablePos = MultiTouchCursorSystem.screenToTable(new Vector2f(evt.getX()+cursorShiftX, evt.getY()+cursorShiftY));
		MultiTouchCursorEvent e = new MultiTouchCursorEvent(cursorId, new Point.Float(tablePos.x, tablePos.y), new Point2D.Float(0,0), (float)0, (double)0);
		input.cursorPressed(e);
		cursor.setOrder(99999999);
	}

	/* (non-Javadoc)
	 * @see apps.mtdesktop.tabletop.TabletopContentManager.DesktopMouseListener#mouseReleased(synergynetframework.appsystem.services.net.localpresence.TableIdentity, apps.mtdesktop.messages.util.MouseEventInfo)
	 */
	public void mouseReleased(TableIdentity tableId, MouseEventInfo evt) {
		if(this.tableId == null || !this.tableId.equals(tableId)) return;
		Point.Float tablePos = MultiTouchCursorSystem.screenToTable(new Vector2f(evt.getX()+cursorShiftX, evt.getY()+cursorShiftY));
		MultiTouchCursorEvent e = new MultiTouchCursorEvent(cursorId, new Point.Float(tablePos.x, tablePos.y), new Point2D.Float(0,0), (float)0, (double)0);
		input.cursorReleased(e);
		cursor.setOrder(99999999);
	}

	/* (non-Javadoc)
	 * @see apps.mtdesktop.tabletop.TabletopContentManager.DesktopMouseListener#mouseClicked(synergynetframework.appsystem.services.net.localpresence.TableIdentity, apps.mtdesktop.messages.util.MouseEventInfo)
	 */
	public void mouseClicked(TableIdentity tableId, MouseEventInfo evt) {
	}

	/* (non-Javadoc)
	 * @see apps.mtdesktop.tabletop.TabletopContentManager.DesktopMouseListener#mouseMoved(synergynetframework.appsystem.services.net.localpresence.TableIdentity, apps.mtdesktop.messages.util.MouseEventInfo)
	 */
	public void mouseMoved(TableIdentity tableId, MouseEventInfo evt) {
		if(this.tableId == null || !this.tableId.equals(tableId)) return;
		Point.Float tablePos = MultiTouchCursorSystem.screenToTable(new Vector2f(evt.getX()+cursorShiftX, evt.getY()+cursorShiftY));
		MultiTouchCursorEvent e = new MultiTouchCursorEvent(cursorId, new Point.Float(tablePos.x, tablePos.y), new Point2D.Float(0,0), (float)0, (double)0);
		cursor.setLocalLocation(evt.getX(), evt.getY());
		input.cursorChanged(e);
		cursor.setOrder(99999999);
	}



	/* (non-Javadoc)
	 * @see apps.mtdesktop.tabletop.TabletopContentManager.DesktopMouseListener#mouseDragged(synergynetframework.appsystem.services.net.localpresence.TableIdentity, apps.mtdesktop.messages.util.MouseEventInfo)
	 */
	public void mouseDragged(TableIdentity tableId, MouseEventInfo evt) {
		if(this.tableId == null || !this.tableId.equals(tableId)) return;
		Point.Float tablePos = MultiTouchCursorSystem.screenToTable(new Vector2f(evt.getX()+cursorShiftX, evt.getY()+cursorShiftY));
		MultiTouchCursorEvent e = new MultiTouchCursorEvent(cursorId, new Point.Float(tablePos.x, tablePos.y), new Point2D.Float(0,0), (float)0, (double)0);
		cursor.setLocalLocation(evt.getX(), evt.getY());
		input.cursorChanged(e);
		cursor.setOrder(99999999);
	}
	
	/**
	 * Sets the orientation.
	 *
	 * @param position the new orientation
	 */
	public void setOrientation(DesktopClient.Position position) {
		if(position.equals(DesktopClient.Position.SOUTH)){
			cursor.setAngle(0);
			cursorShiftX = -cursorWidth/2;
			cursorShiftY = cursorHeight/2;
		}else if(position.equals(DesktopClient.Position.NORTH)){
			cursor.setAngle(180 * FastMath.DEG_TO_RAD);
			cursorShiftX = cursorWidth/2;
			cursorShiftY = -cursorHeight/2;
		}else if(position.equals(DesktopClient.Position.EAST)){
			cursor.setAngle(90 * FastMath.DEG_TO_RAD);
			cursorShiftX = -cursorHeight/2;
			cursorShiftY = -cursorWidth/2;
		}else if(position.equals(DesktopClient.Position.WEST)){
			cursor.setAngle(270 * FastMath.DEG_TO_RAD);
			cursorShiftX = cursorHeight/2;
			cursorShiftY = cursorWidth/2;		}
	}

}
