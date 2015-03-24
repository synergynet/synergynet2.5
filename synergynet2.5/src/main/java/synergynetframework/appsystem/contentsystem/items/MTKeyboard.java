package synergynetframework.appsystem.contentsystem.items;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IMTKeyboardImplementation;
import synergynetframework.jme.gfx.twod.keyboard.MTKeyListener;

/**
 * The Class MTKeyboard.
 */
public class MTKeyboard extends Window implements IMTKeyboardImplementation {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2734963371491448620L;
	
	/** The caps lock on. */
	protected boolean capsLockOn = false;

	/** The current keys pressed. */
	protected List<Integer> currentKeysPressed = new ArrayList<Integer>();

	/** The listeners. */
	protected transient List<MTKeyListener> listeners = new ArrayList<MTKeyListener>();
	
	/**
	 * Instantiates a new MT keyboard.
	 *
	 * @param contentSystem
	 *            the content system
	 * @param name
	 *            the name
	 */
	public MTKeyboard(ContentSystem contentSystem, String name) {
		super(contentSystem, name);
	}

	/**
	 * Adds the key listener.
	 *
	 * @param l
	 *            the l
	 */
	public void addKeyListener(MTKeyListener l) {
		if (!listeners.contains(l)) {
			listeners.add(l);
		}
	}

	/**
	 * Fire key pressed.
	 *
	 * @param evt
	 *            the evt
	 */
	public void fireKeyPressed(KeyEvent evt) {
		currentKeysPressed.add(evt.getKeyCode());
		for (MTKeyListener l : listeners) {
			l.keyPressedEvent(evt);
		}
		if (evt.getKeyCode() == KeyEvent.VK_CAPS_LOCK) {
			capsLockOn = !capsLockOn;
		}
	}

	/**
	 * Fire key released.
	 *
	 * @param evt
	 *            the evt
	 */
	public void fireKeyReleased(KeyEvent evt) {
		currentKeysPressed.remove(evt.getKeyCode());
		for (MTKeyListener l : listeners) {
			l.keyReleasedEvent(evt);
		}
	}

	/**
	 * Gets the current keys pressed.
	 *
	 * @return the current keys pressed
	 */
	public List<Integer> getCurrentKeysPressed() {
		return currentKeysPressed;
	}

	/**
	 * Checks if is caps lock enabled.
	 *
	 * @return true, if is caps lock enabled
	 */
	public boolean isCapsLockEnabled() {
		return capsLockOn;
	}

	/**
	 * Checks if is key pressed.
	 *
	 * @param vk
	 *            the vk
	 * @return true, if is key pressed
	 */
	public boolean isKeyPressed(int vk) {
		return currentKeysPressed.contains(vk);
	}
}
