package apps.projectmanagement.component.ganttchart;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.logging.Logger;

import apps.conceptmap.GraphConfig;

import com.jme.math.Vector3f;
import com.jme.scene.Spatial;

import common.CommonResources;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.Keyboard;
import synergynetframework.appsystem.contentsystem.items.LineItem;
import synergynetframework.appsystem.contentsystem.items.MultiLineTextLabel;
import synergynetframework.appsystem.contentsystem.items.listener.OrthoControlPointRotateTranslateScaleAdapter;
import synergynetframework.appsystem.contentsystem.items.utils.Location;
import synergynetframework.jme.gfx.twod.keyboard.Key;
import synergynetframework.jme.gfx.twod.keyboard.MTKeyListener;


/**
 * The Class InputBox.
 */
public class InputBox {
	
	/** The Constant log. */
	private static final Logger log = Logger.getLogger(InputBox.class.getName());	
	
	/** The content system. */
	protected ContentSystem contentSystem;
	
	/** The note label. */
	protected MultiLineTextLabel noteLabel;
	
	/** The keyboard. */
	protected Keyboard keyboard;
	
	/** The line. */
	protected LineItem line;

	/**
	 * Instantiates a new input box.
	 *
	 * @param contentSystem the content system
	 * @param length the length
	 * @param heigth the heigth
	 */
	public InputBox(ContentSystem contentSystem, int length, int heigth){
		this.contentSystem =contentSystem;
		
		//set note label
		noteLabel = (MultiLineTextLabel)contentSystem.createContentItem(MultiLineTextLabel.class);
		noteLabel.setBorderColour(Color.white);
		noteLabel.setBorderSize(0);
		noteLabel.setBackgroundColour(Color.white);
		noteLabel.setTextColour(Color.black);
		Font font = new Font("Arial", Font.PLAIN, 12);
		noteLabel.setFont(font);		
		noteLabel.setAutoFitSize(false);
		noteLabel.setHeight(heigth);
		noteLabel.setWidth(length);			
	}
	
	/**
	 * Clear.
	 */
	public void clear(){
		if (this.isKeyboradOn())
			this.hideKeyborad();
		
		if (noteLabel.getParent()!=null){
			noteLabel.getParent().removeSubItem(noteLabel, true);
			noteLabel=null;
		}
	}
	
	/**
	 * Gets the input box.
	 *
	 * @return the input box
	 */
	public MultiLineTextLabel getInputBox(){
		return noteLabel;
	}
	
	/**
	 * Sets the input box location.
	 *
	 * @param x the x
	 * @param y the y
	 */
	public void setInputBoxLocation(float x, float y){
		noteLabel.setLocalLocation(x, y);
		this.updateKeyboardLocation();
		updateLine();
	}
	
	/**
	 * Update keyboard location.
	 */
	protected void updateKeyboardLocation(){
		if (keyboard==null) return;
		Spatial inputBoxSpatial = ((Spatial)noteLabel.getImplementationObject());
		Vector3f worldLocation = new Vector3f();
		inputBoxSpatial.getParent().localToWorld(inputBoxSpatial.getLocalTranslation(), worldLocation);
		keyboard.setLocalLocation(worldLocation.x, worldLocation.y-100);
	}
	
	/**
	 * Enable keyborad.
	 *
	 * @param enabled the enabled
	 */
	public void enableKeyborad(boolean enabled){
		if (enabled)
			showKeyborad();
		else
			hideKeyborad();
	}
	
	/**
	 * Show keyborad.
	 */
	public void showKeyborad(){
		if (keyboard!= null) return;
		keyboard = (Keyboard)contentSystem.createContentItem(Keyboard.class);
		keyboard.setOrder(noteLabel.getOrder()+1);
		keyboard.setScale(0.8f);
		keyboard.setKeyboardImageResource(GraphConfig.nodeKeyboardImageResource);
		keyboard.setKeyDefinitions(this.getKeyDefs());
		this.updateKeyboardLocation();
		
		
		Spatial keyboardSpatial = ((Spatial)keyboard.getImplementationObject());
		keyboardSpatial.setZOrder(10020);
		keyboard.setBringToTopable(false);
		
		line = (LineItem)contentSystem.createContentItem(LineItem.class);
		line.setSourceItem(noteLabel);
		line.setSourceLocation(noteLabel.getLocalLocation());
		line.setTargetItem(keyboard);
		line.setTargetLocation(keyboard.getLocalLocation());
		line.setArrowMode(LineItem.NO_ARROWS);
		line.setLineMode(LineItem.SEGMENT_LINE);
		updateLine();
		
		Spatial linepatial = ((Spatial)line.getImplementationObject());
		linepatial.setZOrder(10007);
		line.setBringToTopable(false);
		
		

		keyboard.addKeyListener(new MTKeyListener(){

			@Override
			public void keyReleasedEvent(KeyEvent evt) {
				String text = noteLabel.getText();
				if(text == null) text ="";
				if(evt.getKeyChar() == KeyEvent.VK_ENTER){
					noteLabel.setCRLFSeparatedString(text + evt.getKeyChar());
				}
				else if(evt.getKeyChar() == KeyEvent.VK_BACK_SPACE){
					if(text.length() >0){
						text = text.substring(0,text.length()-1);
						noteLabel.setCRLFSeparatedString(text);
					}
				}
				else if(evt.getKeyChar() != KeyEvent.VK_CAPS_LOCK){
					text = text+evt.getKeyChar();
					noteLabel.setCRLFSeparatedString(text);
				}
				
			}

			@Override
			public void keyPressedEvent(KeyEvent evt) {
				
			}});
		
		keyboard.addOrthoControlPointRotateTranslateScaleListener(new OrthoControlPointRotateTranslateScaleAdapter(){

			@Override
			public void itemTranslated(ContentItem item, float newLocationX,
					float newLocationY, float oldLocationX, float oldLocationY) {
				updateLine();
			}
			
		});
	}
	
	/**
	 * Hide keyborad.
	 */
	public void hideKeyborad(){
		if (this.keyboard!=null){
			keyboard.removeOrthoControlPointRotateTranslateScaleListeners();
			this.contentSystem.removeContentItem(keyboard);
			keyboard = null;
		}
		
		if (this.line!=null){
			this.contentSystem.removeContentItem(line);
			line = null;
		}
	}
	
	/**
	 * Checks if is keyborad on.
	 *
	 * @return true, if is keyborad on
	 */
	public boolean isKeyboradOn(){
		if (keyboard==null)
			return false;
		else
			return true;
	}
	
	/**
	 * Gets the key defs.
	 *
	 * @return the key defs
	 */
	@SuppressWarnings("unchecked")
	private List<Key> getKeyDefs() {
		try {
			ObjectInputStream ois = new ObjectInputStream(CommonResources.class.getResourceAsStream("keyboard.def"));
			return (List<Key>) ois.readObject();
		} catch (FileNotFoundException e) {
			log.warning("Key Defs file has not been found.");
		} catch (IOException e) {
			log.warning("IOException when accessing Key Defs file.");
		} catch (ClassNotFoundException e) {
			log.warning("ClassNotFoundExcetpion when accessing Key Defs file.");
		}
		return null;
	}
	
	/**
	 * Update line.
	 */
	private void updateLine(){
		if (line!=null && keyboard!=null){
			
			Spatial inputBoxSpatial = ((Spatial)noteLabel.getImplementationObject());
			Vector3f worldLocation = new Vector3f();
			inputBoxSpatial.getParent().localToWorld(inputBoxSpatial.getLocalTranslation(), worldLocation);
			
			line.setSourceLocation(new Location(worldLocation.x, worldLocation.y, 0));
			line.setTargetLocation(keyboard.getLocalLocation());
		}
	}

}
