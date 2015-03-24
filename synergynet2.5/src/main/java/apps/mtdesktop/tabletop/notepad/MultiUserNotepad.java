package apps.mtdesktop.tabletop.notepad;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.HtmlFrame;
import synergynetframework.appsystem.contentsystem.items.SimpleButton;
import synergynetframework.appsystem.contentsystem.items.Window;
import synergynetframework.appsystem.contentsystem.items.listener.SimpleButtonListener;
import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import apps.mtdesktop.tabletop.TabletopContentManager.DesktopKeyboardListener;

/**
 * The Class MultiUserNotepad.
 */
public class MultiUserNotepad implements DesktopKeyboardListener {

	/** The color map. */
	protected Map<TableIdentity, Color> colorMap = new HashMap<TableIdentity, Color>();

	/** The doc. */
	protected HtmlFrame doc;

	/** The font size. */
	public int fontSize = 18;

	/** The pad height. */
	public int padHeight = 250;

	/** The pad width. */
	public int padWidth = 300;

	/** The set. */
	private SimpleAttributeSet set = new SimpleAttributeSet();
	
	/** The window. */
	protected Window window;
	
	/**
	 * Instantiates a new multi user notepad.
	 *
	 * @param contentSystem
	 *            the content system
	 */
	public MultiUserNotepad(ContentSystem contentSystem) {
		window = (Window) contentSystem.createContentItem(Window.class);
		doc = (HtmlFrame) contentSystem.createContentItem(HtmlFrame.class);
		doc.setBackgroundColour(Color.white);
		doc.setAutoFitSize(false);
		window.setWidth(padWidth);
		window.setHeight(padHeight);
		doc.setWidth(padWidth - 20);
		doc.setHeight(padHeight - 50);
		window.addSubItem(doc);
		doc.setLocalLocation(0, 20);
		window.setBackgroundColour(Color.red);
		window.setBorderSize(0);
		window.centerItem();
		StyleConstants.setFontSize(set, fontSize);
		
		SimpleButton clear = (SimpleButton) contentSystem
				.createContentItem(SimpleButton.class);
		clear.setAutoFitSize(true);
		clear.setText("Clear");
		clear.setBackgroundColour(Color.white);
		clear.addButtonListener(new SimpleButtonListener() {
			
			@Override
			public void buttonClicked(SimpleButton b, long id, float x,
					float y, float pressure) {
				
			}
			
			@Override
			public void buttonDragged(SimpleButton b, long id, float x,
					float y, float pressure) {
				
			}
			
			@Override
			public void buttonPressed(SimpleButton b, long id, float x,
					float y, float pressure) {
				doc.remove(0, doc.getPane().getDocument().getLength());
			}
			
			@Override
			public void buttonReleased(SimpleButton b, long id, float x,
					float y, float pressure) {
				
			}
			
		});
		window.addSubItem(clear);
		clear.setLocalLocation(0, -100);
	}

	/**
	 * Gets the window.
	 *
	 * @return the window
	 */
	public Window getWindow() {
		return window;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * apps.mtdesktop.tabletop.TabletopContentManager.DesktopKeyboardListener
	 * #keyPressed
	 * (synergynetframework.appsystem.services.net.localpresence.TableIdentity,
	 * java.awt.event.KeyEvent)
	 */
	@Override
	public void keyPressed(TableIdentity tableId, KeyEvent evt) {

	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * apps.mtdesktop.tabletop.TabletopContentManager.DesktopKeyboardListener
	 * #keyReleased
	 * (synergynetframework.appsystem.services.net.localpresence.TableIdentity,
	 * java.awt.event.KeyEvent)
	 */
	@Override
	public void keyReleased(TableIdentity tableId, KeyEvent evt) {
		if (!colorMap.containsKey(tableId)) {
			return;
		}
		Color color = colorMap.get(tableId);
		StyleConstants.setForeground(set, color);
		if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
			doc.insertString(doc.getPane().getDocument().getLength(), "\n", set);
		} else if (evt.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
			doc.remove(doc.getPane().getDocument().getLength() - 1, 1);
		} else if ((evt.getKeyCode() != KeyEvent.VK_CAPS_LOCK)
				&& (evt.getKeyCode() != KeyEvent.VK_SHIFT)
				&& (evt.getKeyCode() != KeyEvent.VK_ALT)
				&& (evt.getKeyCode() != KeyEvent.VK_CONTROL)
				&& !evt.isActionKey()
				&& (evt.getKeyCode() != KeyEvent.VK_ESCAPE)) {
			doc.insertString(doc.getPane().getDocument().getLength(),
					evt.getKeyChar() + "", set);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * apps.mtdesktop.tabletop.TabletopContentManager.DesktopKeyboardListener
	 * #keyTyped
	 * (synergynetframework.appsystem.services.net.localpresence.TableIdentity,
	 * java.awt.event.KeyEvent)
	 */
	@Override
	public void keyTyped(TableIdentity tableId, KeyEvent evt) {

	}
	
	/**
	 * Register user.
	 *
	 * @param tableId
	 *            the table id
	 * @param color
	 *            the color
	 */
	public void registerUser(TableIdentity tableId, Color color) {
		if (colorMap.containsKey(tableId)) {
			colorMap.remove(tableId);
		}
		colorMap.put(tableId, color);
	}
	
	/**
	 * Unregister user.
	 *
	 * @param tableId
	 *            the table id
	 */
	public void unregisterUser(TableIdentity tableId) {
		if (colorMap.containsKey(tableId)) {
			colorMap.remove(tableId);
		}
	}
	
}
