package synergynetframework.appsystem.contentsystem.items;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IMTKeyboardImplementation;
import synergynetframework.jme.gfx.twod.keyboard.MTKeyListener;


public class MTKeyboard extends Window implements IMTKeyboardImplementation{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2734963371491448620L;

	protected boolean capsLockOn = false;
	protected List<Integer> currentKeysPressed = new ArrayList<Integer>();
	protected transient List<MTKeyListener> listeners = new ArrayList<MTKeyListener>();

	public MTKeyboard(ContentSystem contentSystem, String name) {
		super(contentSystem, name);
	}
	
	public void addKeyListener(MTKeyListener l) {
		if(!listeners.contains(l)) listeners.add(l);
	}
	
	public void fireKeyPressed(KeyEvent evt) {
		currentKeysPressed.add(evt.getKeyCode());
		for(MTKeyListener l : listeners) {
			l.keyPressedEvent(evt);
		}
		if(evt.getKeyCode() == KeyEvent.VK_CAPS_LOCK) 
			capsLockOn = !capsLockOn;
	}
	
	public void fireKeyReleased(KeyEvent evt) {
		currentKeysPressed.remove(evt.getKeyCode());
		for(MTKeyListener l : listeners) {
			l.keyReleasedEvent(evt);
		}		
	}
	
	public boolean isKeyPressed(int vk) {
		return currentKeysPressed.contains(vk);
	}
	
	public boolean isCapsLockEnabled(){
		return capsLockOn;
	}
	
	public List<Integer> getCurrentKeysPressed() {
		return currentKeysPressed;
	}
}
