package apps.mtdesktop.tabletop.notepad;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import apps.mtdesktop.tabletop.TabletopContentManager.DesktopKeyboardListener;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.HtmlFrame;
import synergynetframework.appsystem.contentsystem.items.SimpleButton;
import synergynetframework.appsystem.contentsystem.items.Window;
import synergynetframework.appsystem.contentsystem.items.listener.SimpleButtonListener;
import synergynetframework.appsystem.services.net.localpresence.TableIdentity;

public class MultiUserNotepad implements DesktopKeyboardListener{
	
	public int fontSize = 18;
	public int padWidth = 300;
	public int padHeight = 250;
	
	protected Window window;
	protected HtmlFrame doc;
	protected Map<TableIdentity, Color> colorMap = new HashMap<TableIdentity, Color>(); 
	private SimpleAttributeSet set = new SimpleAttributeSet();
    
	public MultiUserNotepad(ContentSystem contentSystem){
		window = (Window)contentSystem.createContentItem(Window.class);
		doc = (HtmlFrame)contentSystem.createContentItem(HtmlFrame.class);
		doc.setBackgroundColour(Color.white);
		doc.setAutoFitSize(false);
		window.setWidth(padWidth);
		window.setHeight(padHeight);
		doc.setWidth(padWidth-20);
		doc.setHeight(padHeight-50);
		window.addSubItem(doc);
		doc.setLocalLocation(0, 20);
		window.setBackgroundColour(Color.red);
		window.setBorderSize(0);
		window.centerItem();
	    StyleConstants.setFontSize(set, fontSize);
	    
	    SimpleButton clear = (SimpleButton) contentSystem.createContentItem(SimpleButton.class);
	    clear.setAutoFitSize(true);
	    clear.setText("Clear");
	    clear.setBackgroundColour(Color.white);
	    clear.addButtonListener(new SimpleButtonListener(){

			@Override
			public void buttonPressed(SimpleButton b, long id, float x,
					float y, float pressure) {
				doc.remove(0, doc.getPane().getDocument().getLength());
			}

			@Override
			public void buttonDragged(SimpleButton b, long id, float x,
					float y, float pressure) {
				 
				
			}

			@Override
			public void buttonReleased(SimpleButton b, long id, float x,
					float y, float pressure) {
				 
				
			}

			@Override
			public void buttonClicked(SimpleButton b, long id, float x,
					float y, float pressure) {
				 
				
			}
	    	
	    });
	    window.addSubItem(clear);
		clear.setLocalLocation(0, -100);
	}
	
	public void registerUser(TableIdentity tableId, Color color){
		if(colorMap.containsKey(tableId)) colorMap.remove(tableId); 
		colorMap.put(tableId, color);
	}
	
	public void unregisterUser(TableIdentity tableId){
		if(colorMap.containsKey(tableId)) 
			colorMap.remove(tableId);
	}

	@Override
	public void keyPressed(TableIdentity tableId, KeyEvent evt) {
		
	}

	@Override
	public void keyReleased(TableIdentity tableId, KeyEvent evt) {
		if(!colorMap.containsKey(tableId)) return;
		Color color = colorMap.get(tableId);
	    StyleConstants.setForeground(set, color);
		if(evt.getKeyCode() == KeyEvent.VK_ENTER){
			doc.insertString(doc.getPane().getDocument().getLength(), "\n", set);
		}
		else if(evt.getKeyCode() == KeyEvent.VK_BACK_SPACE){
			doc.remove(doc.getPane().getDocument().getLength()-1, 1);
		}
		else if(evt.getKeyCode() != KeyEvent.VK_CAPS_LOCK && evt.getKeyCode() != KeyEvent.VK_SHIFT && evt.getKeyCode() != KeyEvent.VK_ALT && evt.getKeyCode() != KeyEvent.VK_CONTROL && !evt.isActionKey() && evt.getKeyCode() != KeyEvent.VK_ESCAPE){
			doc.insertString(doc.getPane().getDocument().getLength(), evt.getKeyChar()+"", set);
		}
	}

	@Override
	public void keyTyped(TableIdentity tableId, KeyEvent evt) {
		
	}

	public Window getWindow() {
		return window;
	}

}
